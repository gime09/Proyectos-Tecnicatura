// src/server/server.js
// ============================================================================
//  Express + Handlebars — Core server (MVP)
//  - ESM, seguridad (helmet con CSP + nonce), logs (morgan), sesiones (connect-mongo)
//  - Handlebars con helpers mínimos
//  - Static de /public (Bootstrap local) y routers
// ============================================================================

import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';

import morgan from 'morgan';
import helmet from 'helmet';
import crypto from 'crypto';
import cookieParser from 'cookie-parser';
import session from 'express-session';
import MongoStore from 'connect-mongo';
import methodOverride from 'method-override';
import { create as createHbs } from 'express-handlebars';

import { hbsHelpers } from '../utils/hbs-helpers.js';
import { config } from '../config/env.js';
import { connectDb } from '../config/db.js';

// Middlewares propios
import { setUserInViews } from '../middlewares/auth.js';
import { notFound, errorHandler } from '../middlewares/errors.js';

// Routers
import indexRouter from '../routes/index.js';
import adminUsersRoutes from '../routes/admin/users.js';
import authRoutes from '../routes/auth.js';
import accountRoutes from '../routes/account.js';
import productsAdminRouter from '../routes/admin/products.js';
import productsRouter from '../routes/products.js';
import cartRouter from '../routes/cart.js';
import adminCategoriesRouter from '../routes/admin/categories.js';
import checkoutRoutes from '../routes/checkout.js';

// __dirname en ESM
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// App
const app = express();
app.set('trust proxy', 1);

// ---------------------------------------------------------------------------
// Nonce por request (antes de Helmet) para permitir scripts inline con seguridad
// ---------------------------------------------------------------------------
app.use((req, res, next) => {
  res.locals.cspNonce = crypto.randomBytes(16).toString('base64');
  next();
});

// ---------------------------------------------------------------------------
// Seguridad: Helmet con CSP que habilita Cloudinary y blob: (preview)
// ---------------------------------------------------------------------------
app.use(
  helmet({
    contentSecurityPolicy: {
      useDefaults: true,
      directives: {
        defaultSrc: ["'self'"],

        scriptSrc: ["'self'", (req, res) => `'nonce-${res.locals.cspNonce}'`],

        // Imágenes locales + data + blob + Cloudinary
        imgSrc: ["'self'", 'data:', 'blob:', 'https://res.cloudinary.com'],

        // 🔽 CSS: tu servidor + inline + Google Fonts + CDNs
        styleSrc: [
          "'self'",
          "'unsafe-inline'",
          'https://fonts.googleapis.com',
          'https://cdnjs.cloudflare.com', // <-- AÑADIDO para Animate.css
          'https://unpkg.com', // <-- AÑADIDO para Bootstrap Icons
        ],
        // 🔽 Específico para <link rel="stylesheet">: tu servidor + inline + Google Fonts + CDNs
        styleSrcElem: [
          "'self'",
          "'unsafe-inline'",
          'https://fonts.googleapis.com',
          'https://cdnjs.cloudflare.com', // <-- AÑADIDO para Animate.css
          'https://unpkg.com', // <-- AÑADIDO para Bootstrap Icons
        ],

        // 🔽 Fuentes: tu servidor + data + Google Fonts + Bootstrap Icons
        fontSrc: [
          "'self'",
          'data:',
          'https://fonts.gstatic.com',
          'https://unpkg.com', // <-- AÑADIDO para las fuentes de los iconos
        ],

        connectSrc: ["'self'"],
        objectSrc: ["'none'"],
        baseUri: ["'self'"],
        frameAncestors: ["'self'"],
      },
    },
    crossOriginResourcePolicy: false,
  }),
);

// Logs en dev
if (!config.isProd) app.use(morgan('dev'));

// Parsers + utilidades
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cookieParser());
app.use(methodOverride('_method'));

// Static (Bootstrap + assets)
app.use(express.static(path.join(__dirname, '..', '..', 'public')));

// ---------------------------------------------------------------------------
// Handlebars (layout + partials + helpers)
// ---------------------------------------------------------------------------
const hbs = createHbs({
  extname: '.hbs',
  layoutsDir: path.join(__dirname, '..', 'views', 'layouts'),
  partialsDir: path.join(__dirname, '..', 'views', 'partials'),
  defaultLayout: 'main',
  helpers: hbsHelpers,
});
app.engine('.hbs', hbs.engine);
app.set('view engine', '.hbs');
app.set('views', path.join(__dirname, '..', 'views'));

// ---------------------------------------------------------------------------
// Sesiones (persistidas en Mongo)
// ---------------------------------------------------------------------------
app.use(
  session({
    name: 'sid',
    secret: config.sessionSecret,
    resave: false,
    saveUninitialized: false,
    store: MongoStore.create({
      mongoUrl: config.mongoUri,
      ttl: 60 * 60 * 24 * 7, // 7 días
      crypto: { secret: config.sessionSecret },
    }),
    cookie: {
      httpOnly: true,
      secure: config.isProd, // solo HTTPS en prod
      sameSite: 'lax',
      maxAge: 1000 * 60 * 60 * 24 * 7,
    },
  }),
);

app.use((req, res, next) => {
  res.locals.request = req;
  next();
});

// Usuario disponible en vistas (navbar, etc.)
app.use(setUserInViews);

// ---------------------------------------------------------------------------
// Rutas
// ---------------------------------------------------------------------------
app.use('/', indexRouter);
app.use('/', authRoutes);
app.use('/admin/users', adminUsersRoutes);
app.use(accountRoutes);
app.use('/admin/products', productsAdminRouter);
app.use('/admin/categories', adminCategoriesRouter);
app.use('/products', productsRouter);
app.use('/cart', cartRouter);
app.use('/', checkoutRoutes);

// 404 + errores centralizados
app.use(notFound);
app.use(errorHandler);

// ---------------------------------------------------------------------------
// Boot
// ---------------------------------------------------------------------------
const start = async () => {
  await connectDb();
  app.listen(config.port, () => {
    console.log(`🚀 Server listening on http://localhost:${config.port} (${config.nodeEnv})`);
  });
};

// Export app for tests. When running under test environment we avoid auto-starting the server
// so test runners can import the app module without triggering a real DB connection or listen.
if (process.env.NODE_ENV !== 'test') {
  start();
}

export default app;
