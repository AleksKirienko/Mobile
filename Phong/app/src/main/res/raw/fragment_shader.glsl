// код фрагментного шейдера
precision mediump float;
varying vec4 varyingColor;
varying vec3 varyingNormal;
varying vec3 varyingPos;
uniform vec3 lightDir;

void main() {
    float Ns = 10.0;// блик
    float kd = 0.5, ks = 0.8;//яркость и контрастность
    vec4 light = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 lightS = vec4(1.0, 1.0, 1.0, 1.0);
    vec3 Nn = normalize(varyingNormal);
    vec3 Ln = normalize(lightDir);
    vec4 diffuse = kd * light * max(dot(Nn, Ln), 0.0);
    vec3 Ref = reflect(Nn, Ln);
    float spec = pow(max(dot(Ref, normalize(varyingPos)), 0.0), Ns);
    vec4 specular = lightS * ks * spec;
    gl_FragColor = varyingColor*diffuse + specular;
}