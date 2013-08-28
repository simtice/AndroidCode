public String myconnector(String url,String id,String pt){
   String result ="";
   try{
HttpParams httpParameters = new BasicHttpParams();
int timeoutConnection = 3000;
HttpConnectionParams.setConnectionTimeout(httpParameters,
timeoutConnection);
DefaultHttpClient httpclient = new DefaultHttpClient(
httpParameters);

HttpPost httpost = new HttpPost(url);
List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
httpParams.add(new BasicNameValuePair(id,pt));
httpost.setEntity(new UrlEncodedFormEntity(httpParams, HTTP.UTF_8));
HttpResponse httpResponse = httpclient.execute(httpost);
if(httpResponse!=null)
{
if(httpResponse.getStatusLine().getStatusCode()==200)
{

result = EntityUtils.toString(httpResponse.getEntity());
result = result.replaceAll("\r\n|\n\r|\n", "");
}
if(httpResponse.getStatusLine().getStatusCode()!=200)
{
myHandler.sendEmptyMessage(2);
}
 }
else{
myHandler.sendEmptyMessage(2);
}
   }catch (ClientProtocolException e) {
System.out
.println("_________ClientProtocolException_________");
myHandler.sendEmptyMessage(2);
} catch (UnsupportedEncodingException e) {
System.out
.println("_________UnsupportedEncodingException_________");
myHandler.sendEmptyMessage(2);


    } catch (IOException e) {
System.out.println("_________IOException_________");
myHandler.sendEmptyMessage(2);

    }
return result;
}

conn.setReadTimeout(30 * 1000);//∂¡»°≥¨ ±