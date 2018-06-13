package cn.easyar.samples.helloarqrcode.rendering;

public class ModelShaderLoader extends ShaderLoaderBase {

    public ModelShaderLoader(String vertexShaderFile, String fragmentShaderFile) {
        super(vertexShaderFile, fragmentShaderFile);
    }

    @Override
    protected void bindAttributes() {
        //super.bindAttribute(0,"position");
        /*TODO - check later*/
    }
}
