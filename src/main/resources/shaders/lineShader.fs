#version 330 core
precision mediump float;

in vec3 tColor;

out vec4 outColor;

void main()
{
	outColor = vec4(tColor, 1.0);
}
