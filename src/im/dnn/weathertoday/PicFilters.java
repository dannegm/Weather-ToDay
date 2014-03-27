package im.dnn.weathertoday;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PicFilters {
	private static int[][][] _BitToMat(Bitmap bmp) {
		int picw = bmp.getWidth(); int pich = bmp.getHeight();
		int[] pix = new int[picw * pich];
		bmp.getPixels(pix, 0, picw, 0, 0, picw, pich);
		int matriz[][][] = new int[picw][pich][4];
		for (int y = 0; y < pich; y++) {
			for (int x = 0; x < picw; x++) {
				int index = y * picw + x;
				matriz[x][y][0] = (pix[index] >> 24) & 0xff;
				matriz[x][y][1] = (pix[index] >> 16) & 0xff;
				matriz[x][y][2] = (pix[index] >> 8) & 0xff;
				matriz[x][y][3] = pix[index] & 0xff;
			}
		}
		return matriz;
	}
	private static Bitmap _MatToBit(int mat[][][], int w, int h) { 
		int[] pix = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int index = y * w + x;
				pix[index] = 0xff000000 | (mat[x][y][1] << 16) | (mat[x][y][2] << 8) | mat[x][y][3];
			}
		}
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		bm.setPixels(pix, 0, w, 0, 0, w, h);
		return bm;
	}
	private static int[][][] _GrayScale(int mat[][][], int w, int h) {
		int matriz[][][] = new int[w][h][4];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int r = mat[x][y][1];
				int g = mat[x][y][2];
				int b = mat[x][y][3];
				int R = (int)(0.299*r + 0.587*g + 0.114*b);
				matriz[x][y][1] = R; matriz[x][y][2] = R; matriz[x][y][3] = R;
			}
		}
		return matriz;
	}
	private static int[][][] _Blur(int ratio, int mat[][][], int w, int h) {
		int r1 = 0, r2 = 0, r3 = 0;
		int salida[][][] = new int[w][h][4];
		for (int y = 1; y < h-1; y++) {
			for (int x = 1; x < w-1; x++) {
				for(int i = -1; i<1; i++) {
					for(int j = -1; j<1; j++) {
						r1 = r1 + mat[x+j][y+i][1];
						r2 = r2 + mat[x+j][y+i][2];
						r3 = r3 + mat[x+j][y+i][3];
					}
				}
				salida[x][y][1] = (int) Math.round(r1/ratio);
				salida[x][y][2] = (int) Math.round(r2/ratio);
				salida[x][y][3] = (int) Math.round(r3/ratio);
			}
			r1 = 0; r2 = 0; r3 = 0;
		}
		return salida;
	}
	public static float Proporcional (float lado1, float lado2, float NuevoLado1) {
		float v1 = (lado2 / lado1) * NuevoLado1;
		v1 = Math.round(v1 * 1) / 1;
		return v1;
	}

	public static Bitmap GraySacale (Bitmap bm) {
		int[][][] mb = _BitToMat(bm);
		int w = bm.getWidth();
		int h = bm.getHeight();
		mb = _GrayScale(mb, w, h);
		return _MatToBit(mb, w, h);
	}
	public static Bitmap Blur (Bitmap bm, int ratio) {
		int[][][] mb = _BitToMat(bm);
		int w = bm.getWidth();
		int h = bm.getHeight();
		mb = _Blur(ratio, mb, w, h);
		return _MatToBit(mb, w, h);
	}
	
	public static Bitmap resize (Bitmap mBitmap, float newWidth, float newHeigth) {
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		
		if (width >= height) {
			newWidth = Proporcional(height, width, newHeigth);
		} else {
			newHeigth = Proporcional(width, height, newWidth);
		}

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeigth) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
	}
}
