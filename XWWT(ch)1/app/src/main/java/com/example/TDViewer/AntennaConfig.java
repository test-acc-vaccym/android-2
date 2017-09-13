package com.example.TDViewer;

import java.util.ArrayList;
import java.util.List;


public class AntennaConfig 
{  
	List<LoadedObjectVertexTexXC> lovo=new ArrayList<LoadedObjectVertexTexXC>();
	//strλ�ļ������ƣ�colorλÿһ�����ֶ�Ӧ����ɫ����Ϊ��ά���飩
	public AntennaConfig(LoadedObjectVertexTexXC[] parts,AntennaSurfaceView mv)
	{
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