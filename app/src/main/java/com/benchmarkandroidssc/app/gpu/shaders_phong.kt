const val PHONG_VERTEX_SHADER = """#version 300 es
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aUV;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;
uniform mat4 uLightSpaceMatrix; 

out vec3 vNormal;
out vec3 vFragPos;
out vec4 vShadowPos;
out vec2 vUV;

void main(){
    vec4 worldPos = uModel * vec4(aPos, 1.0);
    
    vFragPos = worldPos.xyz;
    vUV = aUV;
    
    vNormal = normalize(mat3(uModel) * aNormal);

    vShadowPos = uLightSpaceMatrix * worldPos;

    gl_Position = uProj * uView * worldPos;
}
"""
const val PHONG_FRAGMENT_SHADER = """#version 300 es
precision highp float;

in vec3 vNormal;
in vec3 vFragPos;
in vec4 vShadowPos;
in vec2 vUV;

uniform sampler2D uShadowMap; 
uniform sampler2D uTexture;  

out vec4 fragColor;

float getShadow(vec4 shadowPos) {   
    vec3 projCoords = shadowPos.xyz / shadowPos.w;       
    projCoords = projCoords * 0.5 + 0.5;
   
    if(projCoords.z > 1.0) return 1.0;
    float closestDepth = texture(uShadowMap, projCoords.xy).r;    
    float currentDepth = projCoords.z;    
    float bias = 0.005;
    float shadow = currentDepth - bias > closestDepth ? 0.5 : 1.0;
    
    return shadow;
}

void main() {
    vec3 color = texture(uTexture, vUV).rgb;
    vec3 normal = normalize(vNormal);
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    
    vec3 ambient = 0.3 * color;
    vec3 lightDir = normalize(vec3(5.0, 8.0, 5.0) - vFragPos);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    float shadow = getShadow(vShadowPos);
    vec3 result = ambient + (shadow * diffuse * color);
    fragColor = vec4(result, 1.0);
}
"""