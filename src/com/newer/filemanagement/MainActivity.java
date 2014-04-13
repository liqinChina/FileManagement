package com.newer.filemanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DrawerLayout drawerLayout;
	private ListView listView;
	private ActionBarDrawerToggle drawerToggle;

	private String[] manangementList;
	private ArrayList<HashMap<String, Object>> dataOfList;
	private SimpleAdapter adapterOfList;
	
	ActionBar actionBar;

	private CharSequence drawer_title;
	private CharSequence mtitle;
	private String[] from = { "imageName", "name" };
	private int[] to = { R.id.imageView1, R.id.textView1 };

	File2Fragment file2Fragment;
	EditText editTextNew;
	MenuItem itemNew;
	
	long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mtitle = drawer_title = getTitle();

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ListView) findViewById(R.id.left_drawer);
		dataOfList = new ArrayList<HashMap<String, Object>>();
		loatDrawerListData();
		adapterOfList = new SimpleAdapter(this, dataOfList,
				R.layout.list_item_drawerlist, from, to);
		listView.setAdapter(adapterOfList);

		manangementList = getResources()
				.getStringArray(R.array.management_item);
		// listView.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, manangementList));

		// 设置阴影
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		  actionBar = getActionBar();
		
	
		// 操作栏可返回
		actionBar.setDisplayHomeAsUpEnabled(true);

		// 显示操作栏左侧的<
		actionBar.setHomeButtonEnabled(true);

		listView.setOnItemClickListener(new DrawerItemClickListener());

		// 创建控制之类的按钮
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				actionBar.setTitle(R.string.app_name);
				// invalidateOptionsMenu(); 选项菜单无效
				itemNew.setVisible(true);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

				actionBar.setTitle(R.string.drawer_open);
				// invalidateOptionsMenu();
				itemNew.setVisible(false);
			}

		};

		drawerLayout.setDrawerListener(drawerToggle);

		/*
		 * if (savedInstanceState == null) { selectItem(0); }
		 */
	}

	private void loatDrawerListData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("imageName", R.drawable.ic_action_storage);
		map.put("name", "存储");

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("imageName", R.drawable.ic_action_collection);
		map2.put("name", "文件浏览");

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("imageName", R.drawable.ic_action_favorite);
		map3.put("name", "书签");

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("imageName", R.drawable.ic_action_settings);
		map4.put("name", "设置");

		dataOfList.add(map);
		dataOfList.add(map2);
		dataOfList.add(map3);
		dataOfList.add(map4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		itemNew = menu.findItem(R.id.item_new);
		return true;
	}

	// invalidateOptionsMenu
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// 打开或关闭抽屉
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.item_new:
			AlertDialog.Builder newBuilder = new AlertDialog.Builder(getApplicationContext());
			newBuilder.setTitle("新建文件或文件夹");
			//newBuilder.setIcon(R.drawable.ic_action_new);
			
			  editTextNew = new EditText(getApplicationContext());
			newBuilder.setView(editTextNew);
			
			newBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String fileName = editTextNew.getText().toString();
					try {
						file2Fragment.newFile(fileName);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			newBuilder.show();

			
			break;
		case R.id.item_paste:
			
			try {
				file2Fragment.pasteFile();
				Toast.makeText(getApplicationContext(), "已粘贴", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "粘贴出错了", Toast.LENGTH_LONG).show();
			}

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		mtitle = title;
		getActionBar().setTitle(title);
	}

	// 使用抽屉导航中的图标替代<
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	// 设置状态
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			selectItem(position);

		}

	}

	private void selectItem(int position) {

		if (position == 1) {
			file2Fragment = new File2Fragment();
			Bundle bundle = new Bundle();
			bundle.putInt(File2Fragment.FILE_NUMBER, position);
			file2Fragment.setArguments(bundle);

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, file2Fragment).commit();

			listView.setItemChecked(position, true);

			drawerLayout.closeDrawer(listView);
		}

	}

	public static class File1Fragment extends Fragment {

		public static final String FILE_NUMBER = "file_number";

		public File1Fragment() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.activity_file2, container,
					false);
			int i = getArguments().getInt(FILE_NUMBER);

			return view;
		}

	}

	public static class File2Fragment extends Fragment implements
			OnItemClickListener {

		public static final String FILE_NUMBER = "file_number";
		ListView listView;
		TextView textView;
		ImageView imageView;
		ArrayList<HashMap<String, Object>> dataSet;
		SimpleAdapter adapterOfListView;
		File sdPath;
		File[] files;

		private String[] listviewfrom = { "image", "name" };
		private int[] listviewto = { R.id.imageView1, R.id.textView1 };

		int contextMenuPosition;

		File curFile;
		LayoutInflater inflater;
		EditText editTextUpdate;
		
		File copyFile;

		public File2Fragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// int i = getArguments().getInt(FILE_NUMBER);
			View view = inflater.inflate(R.layout.activity_file2, container,
					false);

			textView = (TextView) view.findViewById(R.id.textView_file2);
			imageView = (ImageView) view.findViewById(R.id.imageView_file2);
			listView = (ListView) view.findViewById(R.id.listView_File2);

			dataSet = new ArrayList<HashMap<String, Object>>();
			loadSdCardFile();

			adapterOfListView = new SimpleAdapter(getActivity()
					.getApplicationContext(), dataSet,
					R.layout.list_item_listview, listviewfrom, listviewto);

			listView.setAdapter(adapterOfListView);

			registerForContextMenu(listView);

			listView.setOnItemClickListener(this);

			inflater = getActivity().getLayoutInflater();

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					doGoBack();
				}
			});

			return view;
		}

		private void loadSdCardFile() {
			sdPath = Environment.getExternalStorageDirectory();

			showParentList();
		}

		private void showParentList() {

			files = sdPath.listFiles();
			dataSet.clear();
			textView.setText(sdPath.getPath());
			for (File file : files) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (file.isFile()) {
					map.put("image", R.drawable.file);
				} else {
					map.put("image", R.drawable.folder);
				}

				map.put("name", file.getName());
				dataSet.add(map);
			}

			
			  if (sdPath.equals(Environment.getExternalStorageDirectory())) {
			 imageView.setVisibility(View.INVISIBLE); }
			 

		}

		// -------------------setOnItemClickListener
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			sdPath = files[position];
			showFileList();

		}

		private void showFileList() {
			if (sdPath.isDirectory()) {

				files = sdPath.listFiles();
				dataSet.clear();
				for (File f : files) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					if (f.isFile()) {
						map.put("image", R.drawable.file);
					} else {
						map.put("image", R.drawable.folder);
					}

					map.put("name", f.getName());
					dataSet.add(map);

				}

				textView.setText(sdPath.getPath());
				imageView.setVisibility(View.VISIBLE);

				adapterOfListView.notifyDataSetChanged();
			} else {

				/*
				 * FileService fileService = new FileService(getBaseContext());
				 * 
				 * 
				 * String text = fileService.openFile("abc.txt");
				 * Toast.makeText(this, "打开" + text , Toast.LENGTH_LONG).show();
				 */

			}

		}

		public void doGoBack() {
			sdPath = sdPath.getParentFile();
			showParentList();

			adapterOfListView.notifyDataSetChanged();

		}
		
		public void pasteFile() throws IOException {
			
			
				FileUtil.cp(copyFile, sdPath);
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (copyFile.isFile()) {
					map.put("image", R.drawable.file);
				} else {
					map.put("image", R.drawable.folder);
				}
				map.put("name", copyFile.getName().toString());
				
				dataSet.add(map);
				
				
				
				adapterOfListView.notifyDataSetChanged();
			

		}

		public  void newFile(String fileName) throws IOException {

			if (fileName.contains(".")) {
				FileUtil.createFile(sdPath, fileName);

				HashMap<String, Object> newMap = new HashMap<String, Object>();
				newMap.put("image", R.drawable.file);
				newMap.put("name", fileName);

				dataSet.add(newMap);
				adapterOfListView.notifyDataSetChanged();
			} else {
				FileUtil.createFolder(sdPath, fileName);

				HashMap<String, Object> newMap = new HashMap<String, Object>();
				newMap.put("image", R.drawable.folder);
				newMap.put("name", fileName);

				dataSet.add(newMap);
				adapterOfListView.notifyDataSetChanged();

			}

		}

		// --------------------------contextMenu

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			getActivity().getMenuInflater().inflate(R.menu.memu_listview, menu);

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			contextMenuPosition = info.position;
			super.onCreateContextMenu(menu, v, menuInfo);

			curFile = new File(sdPath, (String) dataSet
					.get(contextMenuPosition).get("name"));

		}

		@Override
		public boolean onContextItemSelected(MenuItem item) {

			switch (item.getItemId()) {
			case R.id.item_addlable:

				break;
			case R.id.item_copy:
				copyFile = curFile;
				Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_LONG).show();

				break;
			case R.id.item_copyPath:

				break;
			case R.id.item_delete:

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("确定删除文件");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								/*
								 * String item =
								 * dataSet.remove(contextMenuPosition);
								 * dataSet.remove(contextMenuPosition);
								 * adapter.notifyDataSetChanged();
								 * Toast.makeText(getApplicationContext(),
								 * "成功删除  " + item, Toast.LENGTH_LONG).show();
								 */

								FileUtil.rm(curFile);

								dataSet.remove(contextMenuPosition);
								adapterOfListView.notifyDataSetChanged();

								Toast.makeText(getActivity(), "已删除",
										Toast.LENGTH_LONG).show();

							}
						});
				builder.setNegativeButton("取消", null);

				builder.show();

				break;

			case R.id.item_updateName:
				AlertDialog.Builder builder2 = new Builder(getActivity());
				builder2.setTitle("重命名");
				builder2.setMessage("请输入新的文件名");
				editTextUpdate = new EditText(getActivity());
				builder2.setView(editTextUpdate);

				builder2.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String updateName = editTextUpdate.getText()
										.toString();

								HashMap<String, Object> mapUpdate = new HashMap<String, Object>();

								if (curFile.isDirectory()) {
									mapUpdate.put("image", R.drawable.folder);
								} else {
									mapUpdate.put("image", R.drawable.file);
								}

								mapUpdate.put("name", updateName);
								dataSet.set(contextMenuPosition, mapUpdate);
								adapterOfListView.notifyDataSetChanged();

								File updateFile = new File(sdPath, updateName);
								FileUtil.rename(curFile, updateFile);

								Toast.makeText(getActivity(), "修改完成",
										Toast.LENGTH_LONG).show();

							}
						});

				builder2.setNegativeButton("取消", null);

				builder2.show();

				break;
			case R.id.item_info:
				AlertDialog.Builder builder3 = new AlertDialog.Builder(
						getActivity());
				String name = dataSet.get(contextMenuPosition).get("name")
						.toString();
				builder3.setTitle(name);
				builder3.setIcon(R.drawable.info);
				
			/*View viewInfo = inflater.inflate(R.layout.list_item_info,  null);
			TextView textView2 = (TextView) viewInfo.findViewById(R.id.textView2); 
			TextView textView4 = (TextView) viewInfo.findViewById(R.id.textView4); 
			TextView  textView6 = (TextView) viewInfo.findViewById(R.id.textView6);
				  
				 textView2.setText(sdPath.toString());
				  
				 long size = FileUtil.getSize(curFile);
				 textView4.setText(String.valueOf(size));
				
				 //textView6.setText(text);
				 
				  builder3.setView(viewInfo);*/
				
			
				 
				builder3.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder3.show();

				break;

			default:
				break;
			}

			return super.onContextItemSelected(item);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (file2Fragment.sdPath.equals(Environment.getExternalStorageDirectory())) {
				
				
				long secondTime = System.currentTimeMillis();
				
				if ((secondTime - firstTime) > 800) {
					Toast.makeText(this, "再次点击，退出程序。", Toast.LENGTH_LONG).show();
				
					//更新第一次点击的时间
					firstTime =  secondTime ;
				return true;
				} else {
					finish();
				}
				
				
				
				
				
			} else {
				file2Fragment.doGoBack();
			}
			
			
			
			
			return true;
		}

		return false;
	}

}
