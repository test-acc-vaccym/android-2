package com.example.TDViewer;

import com.example.xwwt.R;

public class AntennaInfo 
{
	public static int cuttBoatIndex=2;
	//卫星天线的组件名称
	public static String[][] boatPartNames=
	{
		{
			"base2.obj"
	 	}, 
	 	{ 
	 		"frame2.obj"
		},
		{		
			"disk.obj"
		}
	};
	 
	//卫星天线的组件纹理图片
	public final static int[][] boatTexIdName=
	{
		{
			//R.render.base
			R.drawable.base

		},
		{
			R.drawable.frame
		},
		{
			R.drawable.disk
		}
	};
}