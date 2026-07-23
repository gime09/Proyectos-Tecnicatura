Testing Admin Dashboard (Módulo 5)
=================================

This project contains a set of unit tests for the Admin Dashboard (KPIs + orders).

How to run
----------

1. Set NODE_ENV to `test` so the server does not auto-start (the app module is exported but the server won't listen):

On Windows PowerShell:

```powershell
$env:NODE_ENV = 'test'; npm test
```

What the tests cover
--------------------

- `renderDashboard` — verifies KPIs aggregation handling and latest orders rendering.
- `listOrders` — basic pagination and list rendering.
- `showOrder` — 404 handling for invalid IDs and render of order detail.
- `updateOrderStatus` — status update flow + flash/redirect behavior.

Notes
-----

- Tests are implemented using Node's built-in test runner (`node --test`) which is available on Node >= 18+. Your `package.json` exposes `npm test` that runs `node --test`.
- Tests stub the Mongoose `Order` model methods (aggregate, find, countDocuments, findById, updateOne) to avoid touching a real MongoDB instance.

Next steps (optional)
---------------------

- Add integration tests with `supertest` + a real or in-memory MongoDB (`mongodb-memory-server`) for end-to-end coverage.
- Port tests to Jest if you prefer its mocking/snapshot features.

File locations
--------------

- Tests: `tests/admin.controller.test.js`
- App export: `src/server/server.js` (exports `app` and avoids auto-listen when NODE_ENV=test)

