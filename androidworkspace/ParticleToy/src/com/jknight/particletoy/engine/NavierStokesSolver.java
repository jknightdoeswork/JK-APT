package com.jknight.particletoy.engine;

public class NavierStokesSolver {
	int N;
    float N_INVERSE;
    int SIZE;
    float[] u;
    float[] v;
    float[] u_prev;
    float[] v_prev;
    float[] dense;
    float[] dense_prev;
    
	public NavierStokesSolver(int n) {
		N = n;
		N_INVERSE = 1.0f / N;
		SIZE = (N+2) * (N+2);
		u = new float[SIZE];
	    v = new float[SIZE];
	    u_prev = new float[SIZE];
	    v_prev = new float[SIZE];
	    dense = new float[SIZE];
	    dense_prev = new float[SIZE];
	}

    public float getDx(int x, int y) {
        return u[INDEX(x + 1, y + 1)];
    }

    public float getDy(int x, int y) {
        return v[INDEX(x + 1, y + 1)];
    }

    public void applyForce(int cellX, int cellY, float vx, float vy) {
        float dx = u[INDEX(cellX, cellY)];
        float dy = v[INDEX(cellX, cellY)];
        
        u[INDEX(cellX, cellY)] = (vx != 0) ? vx : dx;
        v[INDEX(cellX, cellY)] = (vy != 0) ? vy : dy;

    }

    public void tick(float dt, float visc, float diff) {
        vel_step(u, v, u_prev, v_prev, visc, dt);
        dens_step(dense, dense_prev, u, v, diff, dt);
    }

    /**
     * All parameters and return values are in normalized form 0.0 - 1.0
     *
     * @param x
     *          original horizontal position
     * @param y
     *          original vertical position
     * @return warped position, inverse to the motion direction
     */
    public float[] getInverseWarpPosition(float x, float y, float scale) {
        float[] result = new float[2];

        int cellX = (int) Math.floor(x * N);
        int cellY = (int) Math.floor(y * N);

        float cellU = (x * N - (cellX)) * N_INVERSE;
        float cellV = (y * N - (cellY)) * N_INVERSE;
     
        cellX += 1;
        cellY += 2;

        result[0] = (cellU > 0.5f) ? lerp(u[INDEX(cellX, cellY)], u[INDEX(cellX + 1,
                cellY)], cellU - 0.5f) : lerp(u[INDEX(cellX - 1, cellY)], u[INDEX(cellX,
                cellY)], 0.5f - cellU);
        result[1] = (cellV > 0.5f) ? lerp(v[INDEX(cellX, cellY)], v[INDEX(cellX,
                cellY + 1)], cellU) : lerp(v[INDEX(cellX, cellY)], v[INDEX(cellX,
                cellY - 1)], 0.5f - cellV);

        result[0] *= -scale;
        result[1] *= -scale;

        result[0] += x;
        result[1] += y;

        return result;
    }

    public float lerp(float x0, float x1, float l) {
        l *= 1;
        return (1 - l) * x0 + l * x1;
    }

    public int INDEX(int i, int j) {
        return i + (N + 2) * j;
    }

    public void SWAP(float[] x0, float[] x) {
        float[] tmp = new float[SIZE];
        System.arraycopy(x0, 0, tmp, 0, SIZE);
        System.arraycopy(x, 0, x0, 0, SIZE);
        System.arraycopy(tmp, 0, x, 0, SIZE);
    }

    void add_source(float[] x, float[] s, float dt) {
        int i, size = (N + 2) * (N + 2);
        for (i = 0; i < size; i++)
            x[i] += dt * s[i];
    }

    void diffuse(int b, float[] x, float[] x0, float diff, float dt) {
        int i, j, k;
        float a = dt * diff * N * N;
        for (k = 0; k < 20; k++) {
            for (i = 1; i <= N; i++) {
                for (j = 1; j <= N; j++) {
                    x[INDEX(i, j)] = (x0[INDEX(i, j)] + a
                            * (x[INDEX(i - 1, j)] + x[INDEX(i + 1, j)] + x[INDEX(i, j - 1)] + x[INDEX(
                                    i, j + 1)]))
                            / (1 + 4 * a);
                }
            }
            set_bnd(b, x);
        }
    }

    void advect(int b, float[] d, float[] d0, float[] u, float[] v, float dt) {
        int i, j, i0, j0, i1, j1;
        float x, y, s0, t0, s1, t1, dt0;
        dt0 = dt * N;
        for (i = 1; i <= N; i++) {
            for (j = 1; j <= N; j++) {
                x = i - dt0 * u[INDEX(i, j)];
                y = j - dt0 * v[INDEX(i, j)];
                if (x < 0.5f)
                    x = 0.5f;
                if (x > N + 0.5f)
                    x = N + 0.5f;
                i0 = (int) x;
                i1 = i0 + 1;
                if (y < 0.5f)
                    y = 0.5f;
                if (y > N + 0.5f)
                    y = N + 0.5f;
                j0 = (int) y;
                j1 = j0 + 1;
                s1 = x - i0;
                s0 = 1 - s1;
                t1 = y - j0;
                t0 = 1 - t1;
                d[INDEX(i, j)] = s0 * (t0 * d0[INDEX(i0, j0)] + t1 * d0[INDEX(i0, j1)])
                        + s1 * (t0 * d0[INDEX(i1, j0)] + t1 * d0[INDEX(i1, j1)]);
            }
        }
        set_bnd(b, d);
    }

    void set_bnd(int b, float[] x) {
        int i;
        for (i = 1; i <= N; i++) {
            x[INDEX(0, i)] = (b == 1) ? -x[INDEX(1, i)] : x[INDEX(1, i)];
            x[INDEX(N + 1, i)] = b == 1 ? -x[INDEX(N, i)] : x[INDEX(N, i)];
            x[INDEX(i, 0)] = b == 2 ? -x[INDEX(i, 1)] : x[INDEX(i, 1)];
            x[INDEX(i, N + 1)] = b == 2 ? -x[INDEX(i, N)] : x[INDEX(i, N)];
        }
        x[INDEX(0, 0)] = 0.5f * (x[INDEX(1, 0)] + x[INDEX(0, 1)]);
        x[INDEX(0, N + 1)] = 0.5f * (x[INDEX(1, N + 1)] + x[INDEX(0, N)]);
        x[INDEX(N + 1, 0)] = 0.5f * (x[INDEX(N, 0)] + x[INDEX(N + 1, 1)]);
        x[INDEX(N + 1, N + 1)] = 0.5f * (x[INDEX(N, N + 1)] + x[INDEX(N + 1, N)]);
    }

    void dens_step(float[] x, float[] x0, float[] u, float[] v, float diff,
            float dt) {
        add_source(x, x0, dt);
        SWAP(x0, x);
        diffuse(0, x, x0, diff, dt);
        SWAP(x0, x);
        advect(0, x, x0, u, v, dt);
    }

    void vel_step(float[] u, float[] v, float[] u0, float[] v0, float visc,
            float dt) {
        add_source(u, u0, dt);
        add_source(v, v0, dt);
        SWAP(u0, u);
        diffuse(1, u, u0, visc, dt);
        SWAP(v0, v);
        diffuse(2, v, v0, visc, dt);
        project(u, v, u0, v0);
        SWAP(u0, u);
        SWAP(v0, v);
        advect(1, u, u0, u0, v0, dt);
        advect(2, v, v0, u0, v0, dt);
        project(u, v, u0, v0);
    }

    void project(float[] u, float[] v, float[] p, float[] div) {
        int i, j, k;
        float h;
        h = 1.0f / N;
        for (i = 1; i <= N; i++) {
            for (j = 1; j <= N; j++) {
                div[INDEX(i, j)] = -0.5f
                        * h
                        * (u[INDEX(i + 1, j)] - u[INDEX(i - 1, j)] + v[INDEX(i, j + 1)] - v[INDEX(
                                i, j - 1)]);
                p[INDEX(i, j)] = 0;
            }
        }
        set_bnd(0, div);
        set_bnd(0, p);
        for (k = 0; k < 20; k++) {
            for (i = 1; i <= N; i++) {
                for (j = 1; j <= N; j++) {
                    p[INDEX(i, j)] = (div[INDEX(i, j)] + p[INDEX(i - 1, j)]
                            + p[INDEX(i + 1, j)] + p[INDEX(i, j - 1)] + p[INDEX(i, j + 1)]) / 4;
                }
            }
            set_bnd(0, p);
        }
        for (i = 1; i <= N; i++) {
            for (j = 1; j <= N; j++) {
                u[INDEX(i, j)] -= 0.5 * (p[INDEX(i + 1, j)] - p[INDEX(i - 1, j)]) / h;
                v[INDEX(i, j)] -= 0.5 * (p[INDEX(i, j + 1)] - p[INDEX(i, j - 1)]) / h;
            }
        }
        set_bnd(1, u);
        set_bnd(2, v);
    }
}
