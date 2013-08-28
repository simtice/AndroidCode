import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * 管理注册的子Activity，提供Activity的跳转
 * 
 * @author skg
 * 
 */
public class ChildActivityManager {
// 没有被提前注册的Activity统一只用这个一个ID
private final String EXTERNAL_ACTIVITY_ID = "ExternalAct";
private Class<? extends AbsActivity> lastExternalAct;// 最后一个被启动的外来Activity
private String externalActPrevId;// 在外来Activity被打开之前显示的Activity的Id

private String prevActId;// 前一显示的view
private String currentActId;
private String defaultActId;
private AbsActivityGroup mAbsGroup;
private ViewGroup containerView;

private class Child {
Class<? extends AbsActivity> childClass;
Object tag;
}

/**
 * ActivityGroup自动管理的子Activity
 */
private HashMap<String, Child> childs;

protected ChildActivityManager(AbsActivityGroup ag, ViewGroup actPlace) {
mAbsGroup = ag;
containerView = actPlace;
childs = new HashMap<String, Child>();
}

/**
 * 添加被管理的子Activity
 * 
 * @param cls
 */
public void addChildActivity(Class<? extends AbsActivity> cls) {
addChildActivity(cls, null, false);
}

/**
 * 添加被管理的子Activity
 * 
 * @param cls
 * @param tag
 */
public void addChildActivity(Class<? extends AbsActivity> cls, Object tag) {
addChildActivity(cls, tag, false);
}

/**
 * 添加被管理的子Activity
 * 
 * @param cls
 * @param tag
 * @param defaultAct
 */
public void addChildActivity(Class<? extends AbsActivity> cls, Object tag,
boolean defaultAct) {
Child child = createChild(cls, tag);
if (child != null)
childs.put(cls.getCanonicalName(), child);
if (defaultAct) {
setDefaultActivity(cls);
}
}

/**
 * 把指定的Activity设置为默认的<br>
 * 改方法不能设置Object的Tag
 * 
 * @see #addChildActivity(Class, Object, boolean)
 * 
 * @param cls
 */
public void setDefaultActivity(Class<? extends AbsActivity> cls) {
if (!childs.containsKey(cls.getCanonicalName())) {
Child child = createChild(cls, null);
childs.put(cls.getCanonicalName(), child);
}
defaultActId = cls.getCanonicalName();
}

/**
 * 根据注册时候的Class移除子Activity
 * 
 * @param cls
 */
public void removeChildActivity(Class<? extends AbsActivity> cls) {
childs.remove(cls.getCanonicalName());
}

/**
 * @see #showActivity(Class, Bundle)
 */
public void showActivity(Class<? extends AbsActivity> cls) {
showActivity(cls, null);
}

/**
 * 根据注册时候的Id来启动Activity
 * 
 * 推荐使用 {@link #setDefaultActivity(Class)}
 * 根据Id如果不能确定是已经注册过的Id，或者任务操作失误，很有可能写错Id，导致找不到注册Activity
 * 
 * @param actId
 */
public void showActivity(String actId, Bundle data) {
showActivity(childs.get(actId).childClass, data);
}
