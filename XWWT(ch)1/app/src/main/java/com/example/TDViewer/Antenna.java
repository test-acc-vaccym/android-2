package com.example.TDViewer;

import java.util.ArrayList;
import java.util.List;

import com.example.TDView.MatrixState;


public class Antenna 
{  
	List<LoadedObjectVertexTexXC> lovo=new ArrayList<LoadedObjectVertexTexXC>();
	//str位文件的名称，color位每一个部分对应的颜色（其为二维数组）
	public Antenna(LoadedObjectVertexTexXC[] parts,AntennaSurfaceView mv,float pitch,float yaw)
	{
		LoadedObjectVertexTexXC[] parts_T=new LoadedObjectVertexTexXC[3];
		parts_T[0]=parts[0];
		parts_T[1]=parts[1];
		parts_T[2]=parts[2];
		
		if (yaw!=0)
		{
				MatrixState.pushMatrix();
				MatrixState.rotate(20, 0, 1, 0);
		}
		
		for(int i=0;i<parts.length;i++)  
		{  

			lovo.add(parts[i]);
			parts[i].initShader(ShaderManager.getCommTextureShaderProgram());
		}
	}  
	public void drawSelf(int[] texId)
	{
		for(int i=0;i<lovo.size();i++)
		{
			lovo.get(i).drawSelf(texId[i]);
		}
	}
}