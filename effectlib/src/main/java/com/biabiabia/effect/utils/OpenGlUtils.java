package com.biabiabia.effect.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.text.TextUtils;
import com.biabiabia.effect.LibApp;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class OpenGlUtils {
	public static final int NO_TEXTURE = -1;
	public static final int NOT_INIT = -1;	
	public static final int ON_DRAWN = 1;

	private static final Map<String, SoftReference<Shader>> cacheShader = new HashMap<String, SoftReference<Shader>>();
	
	public static int loadTexture(final Bitmap img, final int usedTexId) {
		return loadTexture(img, usedTexId, false);
    }
	
	public static int loadTexture(final Bitmap img, final int usedTexId, boolean recyled) {
		if(img == null)
			return NO_TEXTURE; 
        int textures[] = new int[1];
        if (usedTexId == NO_TEXTURE) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img);
            textures[0] = usedTexId;
        }

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        if(recyled){
			img.recycle();
		}
        return textures[0];
    }
	
	public static int loadTexture(final Buffer data, final int width, final int height, final int usedTexId) {
		if(data == null)
			return NO_TEXTURE;
	    int textures[] = new int[1];
	    if (usedTexId == NO_TEXTURE) {
	        GLES20.glGenTextures(1, textures, 0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
	                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
	    } else {
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
	        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width,
	                height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
	        textures[0] = usedTexId;
	    }

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	    return textures[0];
    }
    
	public static int loadTexture(final Buffer data, final int width, final int height, final int usedTexId, final int type) {
		if(data == null)
			return NO_TEXTURE;
	    int textures[] = new int[1];
	    if (usedTexId == NO_TEXTURE) {
	        GLES20.glGenTextures(1, textures, 0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
	                0, GLES20.GL_RGBA, type, data);
	    } else {
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
	        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width,
	                height, GLES20.GL_RGBA, type, data);
	        textures[0] = usedTexId;
	    }

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	    return textures[0];
    }
    
    public static int loadTexture(final Context context, final String name){
		final int[] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);
		if (textureHandle[0] != 0){
			// Read in the resource
			final Bitmap bitmap = getImageFromSD(context, name);
			if(bitmap != null){
				// Bind to the texture in OpenGL
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
				// Set filtering
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
				// Load the bitmap into the bound texture.
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
				// Recycle the bitmap, since its isFriend has been loaded into OpenGL.
				bitmap.recycle();
			}
		}
		if (textureHandle[0] == 0){
			//throw new RuntimeException("Error loading texture.");
		}
		return textureHandle[0];
	}
	
	public static Bitmap getImageFromSD(Context context, String fileName) {
		Bitmap image = null;
	    try{  
			InputStream is = new FileInputStream(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}catch (IOException e){
		}
		return image;
	}

	public static Bitmap getImageFromAssetsFile(Context context, String fileName){
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}catch (IOException e){
		}
		return image;
	}
    
	public static int loadProgram(final String strVSource, final String strFSource) {
        int iVShader;
        int iFShader;
        int iProgId;
		if(TextUtils.isEmpty(strVSource) || TextUtils.isEmpty(strFSource)) {
			return 0;
		}
        int[] link = new int[1];
        iVShader = loadShader(strVSource, GLES20.GL_VERTEX_SHADER);
        if (iVShader == 0) {
            return 0;
        }
        iFShader = loadShader(strFSource, GLES20.GL_FRAGMENT_SHADER);
        if (iFShader == 0) {
            return 0;
        }

        iProgId = GLES20.glCreateProgram();
        GLES20.glAttachShader(iProgId, iVShader);
        GLES20.glAttachShader(iProgId, iFShader);
        GLES20.glLinkProgram(iProgId);
        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, link, 0);
        if (link[0] <= 0) {
		    GLES20.glDeleteProgram(iProgId);
            return 0;
        }
        GLES20.glDeleteShader(iVShader);
        GLES20.glDeleteShader(iFShader);
        return iProgId;
    }
	
	private static int loadShader(final String strSource, final int iType) {
        int[] compiled = new int[1];
        int iShader = GLES20.glCreateShader(iType);
        GLES20.glShaderSource(iShader, strSource);
        GLES20.glCompileShader(iShader);
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            return 0;
        }
        return iShader;
    }
	
	public static int getExternalOESTextureID() {
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);

		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);

		return texture[0];
	}

	public static int getTextureID(){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		return texture[0];
	}

	public static String readShaderFromRawResource(final int resourceId){
		final InputStream inputStream = LibApp.getAppContext().getResources().openRawResource(
				resourceId);
		final InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
		final BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);

		String nextLine;
		final StringBuilder body = new StringBuilder();

		try{
			while ((nextLine = bufferedReader.readLine()) != null){
				body.append(nextLine);
				body.append('\n');
			}
		}
		catch (IOException e){
			return null;
		}finally {
			try{
				inputStream.close();
				inputStreamReader.close();
				bufferedReader.close();
			}catch (Exception e){
			}
		}
		return body.toString();
	}

	public static String readShaderFromSD(Context context, String fileName){
		FileInputStream inputStream = null;
		try{
			inputStream = new FileInputStream(fileName);
		}catch (FileNotFoundException e){
			return null;
		}

		return readShader(context, inputStream);
	}

	public static String readShaderFromAssetsFile(Context context, String fileName){
		InputStream inputStream = null;
		try{
			AssetManager am = context.getResources().getAssets();
			inputStream = am.open(fileName);
		}catch (IOException e){
		}

		if(inputStream == null){
			return null;
		}

		return readShader(context, inputStream);
	}

	private static String readShader(Context context, InputStream inputStream){
		final InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
		final BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);

		String nextLine;
		StringBuilder body = new StringBuilder();
		try{
			while ((nextLine = bufferedReader.readLine()) != null){
				body.append(nextLine);
				body.append('\n');
			}
		}
		catch (IOException e){
			return null;
		}finally {
			try{
				inputStream.close();
				inputStreamReader.close();
				bufferedReader.close();
			}catch (Exception e){
			}
		}
		return body.toString();
	}

	public static String readStringFromSD (String fileName){
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
		BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);

		String nextLine;
		StringBuilder body = new StringBuilder();
		try{
			while ((nextLine = bufferedReader.readLine()) != null){
				body.append(nextLine.trim());
			}
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}finally {
			try{
				inputStream.close();
				inputStreamReader.close();
				bufferedReader.close();
			}catch (Exception e){
			}
		}
		return body.toString();
	}

	public static Shader getCacheShader(String cacheKey) {
		if(cacheKey == null){
			return null;
		}
		synchronized (cacheShader) {
			if(cacheShader.containsKey(cacheKey)) {
				SoftReference<Shader> soft = cacheShader.get(cacheKey);
				if(soft != null) {
					Shader shader = soft.get();
					if(shader != null) {
						return shader;
					} else {
						cacheShader.remove(cacheKey);
					}
				} else {
					cacheShader.remove(cacheKey);
				}
			}
			return null;
		}
	}

	public static void putCacheShader(String cacheKey, Shader shader) {
		if(cacheKey == null || shader == null){
			return;
		}
		synchronized (cacheShader) {
			if(cacheShader.containsKey(cacheKey)) {
				return;
			}else{
				cacheShader.put(cacheKey, new SoftReference<Shader>(shader));
			}
		}
	}

	public static void checkGlError(String op) {
		int error = GLES20.glGetError();
		if (error != GLES20.GL_NO_ERROR) {
			String msg = op + ": glError 0x" + Integer.toHexString(error);
			throw new RuntimeException(msg);
		}
	}

}
