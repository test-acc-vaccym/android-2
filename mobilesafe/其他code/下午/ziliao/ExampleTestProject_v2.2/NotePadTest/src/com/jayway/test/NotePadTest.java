/*
 * This is an example test project created in Eclipse to test NotePad which is a sample 
 * project located in AndroidSDK/samples/android-9/NotePad
 * Just click on File --> New --> Project --> Android Project --> Create Project from existing source and
 * select NotePad.
 * 
 * Then you can run these test cases either on the emulator or on device. You right click
 * the test project and select Run As --> Run As Android JUnit Test
 * 
 * @author Renas Reda, renas.reda@jayway.com
 * 
 */

package com.jayway.test;

import com.example.android.notepad.NotesList;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;


public class NotePadTest extends ActivityInstrumentationTestCase2<NotesList>{
	
	//机器人
	private Solo solo;

	public NotePadTest() {
		super("com.example.android.notepad", NotesList.class);
		
	}
	//开始测试  
	 public void setUp() throws Exception {
		 //初始化机器人
		 solo = new Solo(getInstrumentation(), getActivity());
		  }

	 //冒烟测试   mokey猴子测试
	 @Smoke
	 public void testAddNote() throws Exception {
		 //点击menu菜单
		 solo.clickOnMenuItem("Add note");
		 //跳转到哪个activity
		 solo.assertCurrentActivity("Expected NoteEditor activity", "NoteEditor"); //Assert that NoteEditor activity is opened
		 //输入文字
		 solo.enterText(0, "Note 1"); //In text field 0, add Note 1
		 //按下返回键
		 solo.goBack(); //Go back
		 solo.clickOnMenuItem("Add note"); //Clicks on menu item 
		 solo.enterText(0, "Note 2"); //In text field 0, add Note 2
		 //跳转到哪个activity
		 solo.goBackToActivity("NotesList"); //Go back to first activity named "NotesList"
		 boolean expected = true;
		 boolean actual = solo.searchText("Note 1") && solo.searchText("Note 2");
		 assertEquals("Note 1 and/or Note 2 are not found", expected, actual); //Assert that Note 1 & Note 2 are found
		
	 }
	
	@Smoke 
	public void testEditNote() throws Exception {
		//点击第二行
		solo.clickInList(2); // Clicks on the second list line
		//横屏
		solo.setActivityOrientation(Solo.LANDSCAPE); // Change orientation of activity
		solo.clickOnMenuItem("Edit title"); // Change title
		solo.enterText(0, " test"); //In first text field (0), add test. 
		solo.goBackToActivity("NotesList");
		boolean expected = true;
		boolean actual = solo.searchText("(?i).*?note 1 test"); // (Regexp) case insensitive												// insensitive
		assertEquals("Note 1 test is not found", expected, actual); //Assert that Note 1 test is found

	}
	

	@Smoke
	 public void testRemoveNote() throws Exception {
		 solo.clickOnText("(?i).*?test.*");   //(Regexp) case insensitive/text that contains "test"
		 solo.clickOnMenuItem("Delete");   //Delete Note 1 test
		 boolean expected = false;   //Note 1 test & Note 2 should not be found
		 boolean actual = solo.searchText("Note 1 test");
		 assertEquals("Note 1 Test is found", expected, actual);  //Assert that Note 1 test is not found
		 solo.clickLongOnText("Note 2");
		 solo.clickOnText("(?i).*?Delete.*");  //Clicks on Delete in the context menu
		 actual = solo.searchText("Note 2");
		 assertEquals("Note 2 is found", expected, actual);  //Assert that Note 2 is not found
	 }

	//停止测试
	@Override
	public void tearDown() throws Exception {
		try {
			//机器人的回收
			solo.finalize(); 	//Robotium will finish all the activities that have been open
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//关闭当前的activity
		getActivity().finish();
		super.tearDown();
	} 
}
