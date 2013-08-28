/**
 * 根据注册时候的Id来启动Activity
 * 
 * @see #showActivity(String, Bundle)
 * @param actId
 */
public void showActivity(String actId) {
showActivity(actId, null);
}

/**
 * 根据Activity类启动显示Activity的View到绑定的ViewGroup中
 * 
 * @param cls
 * @param data
 */
public void showActivity(Class<? extends AbsActivity> cls, Bundle data) {
View targetView = null;
// 没有被注册的Activity会被当做外来Activity来处理
String actId = cls.getCanonicalName();
if (childs.containsKey(actId)) {// 已经注册
if (data != null && mAbsGroup.getActivity(actId) != null) {
/**
 * 如果数据不为null，并且该Id的Activity已经被启动过了，
 * 为了保证数据可以从onCreate方法中到达Activity必须先destroyActivity
 */
mAbsGroup.destroyActivity(actId);
}
} else {// 没有注册的
actId = EXTERNAL_ACTIVITY_ID;
if (mAbsGroup.getActivity(actId) != null) {
/**
 * 单独启动一个AbsActivity的子类,这种启动将在被当做类似于插件的性质，在后退的时候会别remove掉,
 * 如果中途切换到默认的RootView
 * (Home，Rank...)当前的也会被移除，便面过多的不必要的缓存数据,不能无限制的启动
 */
mAbsGroup.destroyActivity(actId);
}
lastExternalAct = cls;// 保存外来Activity的Class
externalActPrevId = currentActId;// 保存当前的RootViewId,在back的时调用showPrevView时候用
}
mAbsGroup.startActivity(actId, cls, data);
Activity act = mAbsGroup.getActivity(actId);
if (act instanceof ActivitysHolder) {
targetView = ((ActivitysHolder) act).getActivityView();
} else {
targetView = act.getWindow().getDecorView();
}
changeView(targetView, actId);
}

/**
 * 显示默认的Activity
 * 
 * @see #showActivity(Class, Bundle)
 */
public boolean showDefaultActivity() {
if (defaultActId != null) {
Class<? extends AbsActivity> cls = childs.get(defaultActId).childClass;
if (cls != null) {
showActivity(cls);
return true;
}
}
return false;
}

/**
 * 显示上一个Activity
 * 
 * @see #showActivity(Class, Bundle)
 */
public void showPrevActivity() {
if (prevActId == currentActId)
return;
Class<? extends AbsActivity> cls;
if (currentActId != null && currentActId.endsWith(EXTERNAL_ACTIVITY_ID)) {
// 如果当前是外来Activity，在显示一个Activity的时候要销毁这个Act
destroyExternalActivity();
}
if (prevActId.equals(EXTERNAL_ACTIVITY_ID)) {
// 如果前一个View是外来Activity就先从ActivityGroup中移除，在切换RootView
destroyExternalActivity();
cls = lastExternalAct;
} else {
cls = childs.get(prevActId).childClass;
}
showActivity(cls);
}

/**
 * 判断当前是不否是在指定的Activity
 * 
 * @param act
 * @return
 */
public boolean isIn(Class<? extends AbsActivity> actClass) {
String id = actClass.getCanonicalName();
if (currentActId != null && id.endsWith(currentActId)) {
return true;
}
return false;
}

/**
 * 判断是不是在外来Activity的界面
 * 
 * @return
 */
public boolean isInExternalAct() {
return isIn(EXTERNAL_ACTIVITY_ID);
}

/**
 * 判断当前的view是不是需要的view
 * 
 * @param rootViewName
 * @return
 */
public boolean isIn(String actId) {
if (currentActId != null && currentActId.equals(actId))
return true;
return false;
}
 
/**
 * 获取当前Activity的Id
 * 
 * @return
 */
public String getCurrentActivityId() {
return currentActId;
}

/**
 * 获取前一次显示的Activity的Id
 * 
 * @return
 */
public String getPrevActivityId() {
return prevActId;
}

/**
 * 状态监听
 * 
 * @author skg
 * 
 */
public interface StatusListener {
public void onChildChanged(String actId, Object actTag);
}

private StatusListener mStatusListener;

/**
 * 设置状态监听器
 * 
 * @param sl
 */
public void setStatusListener(StatusListener sl) {
mStatusListener = sl;
}

private Child createChild(Class<? extends AbsActivity> cls, Object tag) {
if (cls == null)
return null;
Child child = new Child();
child.childClass = cls;
child.tag = tag;
return child;
}

/**
 * 分发Activity状态改变
 * 
 * @param actId
 */
private void dispatchChildChanged(String actId, Object actTag) {
if (mStatusListener != null)
mStatusListener.onChildChanged(actId, actTag);
}

/**
 * 填充View到View容器内
 * 
 * @param childClass
 * @param targetActId
 */
private void changeView(View actView, String actId) {
if (actView == null)
return;
if (containerView.getChildCount() > 0)
containerView.removeAllViews();
containerView.addView(actView);
setCurrentInfo(actId);
Child child = childs.get(actId);
Object tag = null;
if (child != null) {
tag = child.tag;
}
dispatchChildChanged(actId, tag);
}

/**
 * 设置当前的RootViewId
 * 
 * @param currActId
 */
private void setCurrentInfo(String currActId) {
if (currActId == null)
return;
if (currentActId != null && currActId.endsWith(currentActId)) {
return;
}
if (currActId.endsWith(EXTERNAL_ACTIVITY_ID)) {
prevActId = externalActPrevId;
} else {
prevActId = currentActId;
}
currentActId = currActId;
}

/**
 * 销毁外来Activity
 */
private void destroyExternalActivity() {
mAbsGroup.destroyActivity(EXTERNAL_ACTIVITY_ID);
lastExternalAct = null;
externalActPrevId = null;
}
}
 
