package oxygen.util;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glMultMatrix;

public class GLUtil {
	private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private static final float[] IDENTITY_MATRIX = new float[] {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    private static void makeIdentity() {
        int oldPos = matrix.position();
        matrix.put(IDENTITY_MATRIX);
        matrix.position(oldPos);
    }

	public static void perspective(float fovY, float aspectX, float aspectY, float zNear, float zFar) {
		float sine, cotangent, deltaZ;
		float radians = fovY / 2 * 3.1415927f / 180;

		deltaZ = zFar - zNear;
		sine = (float) Math.sin(radians);

		if ((deltaZ == 0) || (sine == 0) || (aspectX == 0) || (aspectY == 0)) {
			return;
		}

		cotangent = (float) Math.cos(radians) / sine;

        makeIdentity();

		matrix.put(0, cotangent / aspectX);
		matrix.put(4 + 1, cotangent / aspectY);
		matrix.put(2 * 4 + 2, -(zFar + zNear) / deltaZ);
		matrix.put(2 * 4 + 3, -1);
		matrix.put(3 * 4 + 2, -2 * zNear * zFar / deltaZ);
		matrix.put(3 * 4 + 3, 0);

		glMultMatrix(matrix);
	}
}
