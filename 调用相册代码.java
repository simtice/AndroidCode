Intent iImage = new Intent(Intent.ACTION_GET_CONTENT);
iImage.addCategory(Intent.CATEGORY_OPENABLE);
iImage.setType("image/*");
startActivityForResult(iImage, RESULT_OPEN_IMAGE); 


大连-飞雪轩主(105411322)10:46:09
返回时
// 获得图片的uri
Uri uri = data.getData();
Cursor cursor = getContentResolver().query(uri, null, null, null, null);
cursor.moveToFirst();
String path = cursor.getString(1); // 文件路径 
