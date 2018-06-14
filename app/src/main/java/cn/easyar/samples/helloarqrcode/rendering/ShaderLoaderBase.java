package cn.easyar.samples.helloarqrcode.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ShaderLoaderBase {

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderLoaderBase(Context context, int vertexShaderFileRes, int fragmentShaderFileRes){
        vertexShaderId = loadShader(context, vertexShaderFileRes, GLES20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(context, fragmentShaderFileRes, GLES20.GL_FRAGMENT_SHADER);
        programId = GLES20.glCreateProgram();
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        GLES20.glLinkProgram(programId);
        GLES20.glValidateProgram(programId);
    }

    public int getProgram(){
        return programId;
    }

    public void start(){
        GLES20.glUseProgram(programId);
    }

    public void stop(){
        GLES20.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        GLES20.glDetachShader(programId, vertexShaderId);
        GLES20.glDetachShader(programId, fragmentShaderId);
        GLES20.glDeleteShader(vertexShaderId);
        GLES20.glDeleteShader(fragmentShaderId);
        GLES20.glDeleteProgram(programId);
    }

    /* Takes in the number of the attribute list in the VAO that we want to bind and it will take the variable name in the
    * shader code that we want to bind that attribute to */
    protected void bindAttribute(int attribute, String variableName){
        GLES20.glBindAttribLocation(programId, attribute, variableName);
    }


    /** Relevant info
     * @param type could be one of {@link GLES20#GL_VERTEX_SHADER} or {@link GLES20#GL_FRAGMENT_SHADER}
     * */
    private static int loadShader(Context context, int rawRes, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{

            InputStream inputStream = context.getResources().openRawResource(rawRes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            /*BufferedReader reader = new BufferedReader(new FileReader(file));*/

            String line;
            while ((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }catch (IOException e){
            Log.e("ShaderError","Could not read the shader file");
            return -1;
        }

        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, shaderSource.toString());
        GLES20.glCompileShader(shaderID);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == GLES20.GL_FALSE){
            Log.e("ShaderError","Could not compile the shader");
            return -1;
        }

        return shaderID;
    }
}
