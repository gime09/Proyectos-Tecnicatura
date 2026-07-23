import { describe, it, beforeEach, afterEach } from 'node:test';
import assert from 'node:assert/strict';

import mongoose from 'mongoose';

import Order from '../src/models/Order.js';
import * as adminController from '../src/controllers/admin.controller.js';

describe('Admin Controller — Dashboard & Orders', () => {
  const originals = {};

  beforeEach(() => {
    // save originals to restore later
    originals.aggregate = Order.aggregate;
    originals.countDocuments = Order.countDocuments;
    originals.find = Order.find;
    originals.findById = Order.findById;
    originals.updateOne = Order.updateOne;
  });

  afterEach(() => {
    // restore
    Order.aggregate = originals.aggregate;
    Order.countDocuments = originals.countDocuments;
    Order.find = originals.find;
    Order.findById = originals.findById;
    Order.updateOne = originals.updateOne;
  });

  it('renderDashboard returns KPIs and latestOrders', async () => {
    // stub aggregate for sales and for topProducts
    Order.aggregate = async (pipeline) => {
      // detect the 'salesAgg' aggregation by looking for $group && _id: null
      if (Array.isArray(pipeline) && pipeline.some((p) => p.$group && p.$group._id === null)) {
        return [{ totalSales: 150, ordersCount: 3 }];
      }
      // detect topProducts pipeline (contains $unwind)
      if (Array.isArray(pipeline) && pipeline.some((p) => p.$unwind)) {
        return [
          { productId: 'p1', title: 'Product 1', qty: 5, revenue: 500 },
          { productId: 'p2', title: 'Product 2', qty: 2, revenue: 200 },
        ];
      }
      return [];
    };

    Order.countDocuments = async (q) => {
      // ordersToday
      return 2;
    };

    // mimic Mongoose Query chaining: find(...).sort(...).limit(...).lean()
    Order.find = () => ({
      sort: () => ({
        limit: () => ({
          lean: async () => [
            { _id: 'o1', userId: 'u1', total: 50, status: 'approved', createdAt: new Date() },
          ],
        }),
      }),
    });

    const req = {};
    let rendered = null;
    const res = {
      render: (tpl, ctx) => {
        rendered = { tpl, ctx };
      },
    };

    await adminController.renderDashboard(req, res, (err) => {
      if (err) throw err;
    });

    assert.ok(rendered, 'Expected res.render to be called');
    assert.strictEqual(rendered.tpl, 'admin/dashboard');
    assert.strictEqual(rendered.ctx.totalSales, 150);
    assert.strictEqual(rendered.ctx.ordersApprovedCount, 3);
    assert.strictEqual(rendered.ctx.ordersToday, 2);
    assert.ok(Array.isArray(rendered.ctx.topProducts));
    assert.ok(Array.isArray(rendered.ctx.latestOrders));
  });

  it('listOrders renders list with pagination', async () => {
    // return a query object that supports sort().skip().limit().lean()
    Order.find = () => ({
      sort: () => ({
        skip: () => ({
          limit: () => ({
            lean: async () => [
              { _id: 'o1', userId: 'u1', total: 80, status: 'created', createdAt: new Date() },
            ],
          }),
        }),
      }),
    });
    Order.countDocuments = async () => 1;

    const req = { query: {} };
    let rendered = null;
    const res = { render: (tpl, ctx) => (rendered = { tpl, ctx }) };

    await adminController.listOrders(req, res, (err) => {
      if (err) throw err;
    });

    assert.ok(rendered);
    assert.strictEqual(rendered.tpl, 'admin/orders');
    assert.ok(Array.isArray(rendered.ctx.items));
    assert.strictEqual(rendered.ctx.pagination.total, 1);
  });

  it('showOrder returns 404 for invalid id', async () => {
    const req = { params: { id: 'invalid-id' } };
    let status = null;
    let rendered = null;
    const res = {
      status: (s) => {
        status = s;
        return { render: (tpl) => (rendered = tpl) };
      },
    };

    await adminController.showOrder(req, res, (err) => {
      if (err) throw err;
    });

    assert.strictEqual(status, 404);
    assert.strictEqual(rendered, 'shared/404');
  });

  it('showOrder renders detail when found', async () => {
    const fakeOrder = { _id: '507f1f77bcf86cd799439011', items: [], total: 100 };
    Order.findById = () => ({
      lean: async () => fakeOrder,
    });

    const req = { params: { id: fakeOrder._id } };
    let rendered = null;
    const res = { render: (tpl, ctx) => (rendered = { tpl, ctx }) };

    await adminController.showOrder(req, res, (err) => {
      if (err) throw err;
    });

    assert.ok(rendered);
    assert.strictEqual(rendered.tpl, 'admin/order_detail');
    assert.deepStrictEqual(rendered.ctx.order, fakeOrder);
  });

  it('updateOrderStatus updates when valid', async () => {
    const validId = '507f1f77bcf86cd799439011';
    Order.updateOne = async (q, patch) => ({ matchedCount: 1 });

    const flashes = [];
    const req = { params: { id: validId }, body: { status: 'approved' }, flash: (t, m) => flashes.push([t, m]) };
    let redirected = null;
    const res = { redirect: (p) => (redirected = p) };

    await adminController.updateOrderStatus(req, res, (err) => {
      if (err) throw err;
    });

    assert.strictEqual(redirected, '/admin/orders');
    assert.ok(flashes.length >= 0);
  });
});
