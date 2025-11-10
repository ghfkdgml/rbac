import http from "k6/http";
import { check, sleep } from "k6";

export const options = { vus: 10, duration: "10s" };

export default function () {
  const base = __ENV.BASE || "http://localhost:8080";
  const role = __ENV.ROLE || "A";
  const plan = __ENV.PLAN || "PRO";
  const headers = {
    "Content-Type": "application/json",
    "X-User-Id": `u${__VU}`,
    "X-Role": role,
    "X-Plan": plan,
  };
  const title = `p-${__VU}-${Date.now()}`;
  const create = http.post(`${base}/projects`, JSON.stringify({ title }), {
    headers,
  });
  check(create, {
    "create status 200 or 403": (r) => r.status === 200 || r.status === 403,
  });

  if (create.status === 200) {
    const id = create.json().data.id;
    const getr = http.get(`${base}/projects/${id}`, {
      headers: { ...headers, "X-Role": "D" },
    });
    check(getr, { "read ok": (r) => r.status === 200 });
  }
  sleep(0.1);
}
