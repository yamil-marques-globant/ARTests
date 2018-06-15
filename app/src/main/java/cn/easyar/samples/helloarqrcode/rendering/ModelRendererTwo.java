package cn.easyar.samples.helloarqrcode.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import cn.easyar.Matrix44F;
import cn.easyar.Vec2F;
import cn.easyar.samples.helloarqrcode.R;

public class ModelRendererTwo {

    /*Basic data*/
    private final String modelName = "andy";
    private final int vertexShaderRes = R.raw.vertex_shader_two;
    private final int fragmentShaderRes = R.raw.fragment_shader_two;

    /*Loaders*/
    private ModelLoader modelLoader;
    private ModelShaderLoader modelShaderLoader;

    /*Matrix*/
    float[] modelMat, viewMat, projMat;

    /*Shaders data handlers*/
    private int coordHandler, colorHandler, transfHandler, projHandler;

    private int animcount = 1;
    private int angle = 0;

    public ModelRendererTwo(Context context) throws IOException {

        /*Model init*/
        modelLoader = new ModelLoader(context);
        modelLoader.load(modelName);

        /*Matrix init*/
        modelMat = new float[16];
        viewMat = new float[16];
        projMat = new float[16];

        /*Shader init*/
        modelShaderLoader = new ModelShaderLoader(context, vertexShaderRes, fragmentShaderRes);
        modelShaderLoader.start();
        /*Shader data handler setup*/

        coordHandler = GLES20.glGetAttribLocation(modelShaderLoader.getProgram(), "coord");
        colorHandler = GLES20.glGetAttribLocation(modelShaderLoader.getProgram(), "color");
        transfHandler = GLES20.glGetUniformLocation(modelShaderLoader.getProgram(), "trans");
        projHandler = GLES20.glGetUniformLocation(modelShaderLoader.getProgram(), "proj");


    }

    public void cleanUp(){
        /*TODO check if more data is needed to be clear*/
        modelShaderLoader.cleanUp();
    }

    public void render(Matrix44F projectionMatrix, Matrix44F cameraview, Vec2F size){
        /*Color*/
        /*GLES20.glEnableVertexAttribArray(colorHandler);
        GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_BYTE, false, 0, modelLoader.vertsBuffer);*/

        /*Coord*/
        GLES20.glEnableVertexAttribArray(coordHandler);
        GLES20.glVertexAttribPointer(coordHandler, 3, GLES20.GL_FLOAT, false, 0, modelLoader.vertsBuffer);

        /*Uniform matrix*/
        Matrix.rotateM(cameraview.data, 0, 100, 1f, 0, 0);
        Matrix.rotateM(cameraview.data, 0, getRotation(), 0, 1f, 0);
        Matrix.scaleM(cameraview.data, 0, 5,5,5);
        GLES20.glUniformMatrix4fv(transfHandler, 1, false, cameraview.data, 0);
        GLES20.glUniformMatrix4fv(projHandler, 1, false, projectionMatrix.data, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelLoader.numFaces * 3, GLES20.GL_UNSIGNED_SHORT, modelLoader.indicesBuffer);
    }

    private int getRotation(){
        if (animcount % 2 == 0){
            animcount = 1;
            angle++;
            if(angle > 360){
                angle = 0;
            }
        }else {
            animcount++;
        }

        return angle;
    }

}
