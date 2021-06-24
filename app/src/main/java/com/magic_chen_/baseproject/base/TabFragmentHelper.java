package com.magic_chen_.baseproject.base;

import android.util.SparseArray;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public abstract class TabFragmentHelper {

    static protected final int BASE_TAB_FRAGMENT_DEFAULT_TYPE = -1;

    private int mCurrentType = BASE_TAB_FRAGMENT_DEFAULT_TYPE;
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private SparseArray<View> mTabs = new SparseArray<>();
    private FragmentManager mFragmentManager = null;

    public TabFragmentHelper() {
    }

    public TabFragmentHelper(FragmentManager fm) {
        mFragmentManager = fm;
    }

    /**
     * 返回tab fragment布局id
     * @return
     */
    protected abstract int getTabFrameLayout();

    /**
     * 创建子tab fragment
     * @return
     */
    protected abstract Fragment createFragment(int type);

    /**
     * tab状态发生变化
     * @return
     */
    protected abstract void onTabSelected(int type, View tab, boolean selected);

    public FragmentTransaction obtainFragmentTransaction(int fromType, int toType) {
        return mFragmentManager.beginTransaction();
    }

    /**
     * tab和type进行关联，type值不能为-1
     * @return
     */
    public void addTab(int type, View tab) {
        if (type != BASE_TAB_FRAGMENT_DEFAULT_TYPE && tab != null) {
            mTabs.put(type, tab);
        }
    }

    /**
     * 清除tab和type的关联，一般在初始化时，addTab()之前调用
     * @return
     */
    public void clearTabs() {
        mTabs.clear();
    }

    /**
     * 清除数据
     * @return
     */
    public void clear() {
        mTabs.clear();
        mFragments.clear();
    }

    /**
     * 获取当前的tab。若没调用switchFragment()进行初始化，则返回null
     * @return
     */
    public View getCurrentTab() {
        if (mCurrentType == BASE_TAB_FRAGMENT_DEFAULT_TYPE) {
            return null;
        }
        return mTabs.get(mCurrentType);
    }

    /**
     * 获取当前的type。若没调用switchFragment()进行初始化，则返回-1
     * @return
     */
    public int getCurrentType() {
        return mCurrentType;
    }

    public Fragment getCurrentFragment() {
        if (mCurrentType == BASE_TAB_FRAGMENT_DEFAULT_TYPE) {
            return null;
        }
        return getFragment(mCurrentType);
    }

    /**
     * 根据tab获取tab fragment
     * @return
     */
    public Fragment getFragment(View tab) {
        int index = mTabs.indexOfValue(tab);
        if (index >= 0) {
            return getFragment(mTabs.keyAt(index));
        }
        return null;
    }

    /**
     * 根据type获取tab fragment
     * @return
     */
    public Fragment getFragment(int type) {
        return getFragment(type, false);
    }

    /**
     * 根据tab切换tab fragment
     * @return
     */
    public void switchFragment(View tab) {
        int index = mTabs.indexOfValue(tab);
        if (index >= 0) {
            switchFragment(mTabs.keyAt(index));
        }
    }

    /**
     * 根据type切换tab fragment
     * @return
     */
    public void switchFragment(int type) {
        if (type == BASE_TAB_FRAGMENT_DEFAULT_TYPE || mCurrentType == type) {
            return;
        }

        Fragment fragment = getFragment(type, true);
        if (fragment == null) {
            return;
        }

        Fragment fragmentLast = getFragment(mCurrentType, false);
        if (fragmentLast != fragment) {
            if (fragmentLast != null) {
                fragmentLast.onPause();
            }
            if (fragment.isAdded()) {
                fragment.onResume();
            }

            FragmentTransaction ft = obtainFragmentTransaction(mCurrentType, type);
            if (fragmentLast != null) {
                if (fragment.isAdded()) {
                    ft.hide(fragmentLast).show(fragment).commit();
                } else {
                    ft.hide(fragmentLast).add(getTabFrameLayout(), fragment).show(fragment).commit();
                }
            } else {
                if (fragment.isAdded()) {
                    ft.show(fragment).commit();
                } else {
                    ft.add(getTabFrameLayout(), fragment).show(fragment).commit();
                }
            }
        }
        setTabState(type);
    }

    public void preLoadFragment(final int type, final int delay) {
        preLoadFragment(type, type, delay);
    }

    public void preLoadFragment(final int startType, final int finishType, final int delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = startType; i <= finishType; i++) {
                    BaseFragment fragment = (BaseFragment)getFragment(i);
                    if (fragment == null) {
                        fragment = (BaseFragment)getFragment(i, true);
                        if (fragment != null && !fragment.isAdded()) {
                            FragmentTransaction fragmentTransaction = obtainFragmentTransaction(-1, -1);
                            fragmentTransaction.add(getTabFrameLayout(), fragment).commit();
                        }
                    }
                }
            }
        }).start();
    }

    public void removeAllFragment(){
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            FragmentTransaction fragmentTransaction =  obtainFragmentTransaction(-1, -1);
            fragmentTransaction.remove(fragment).commit();
        }
    }

    private Fragment getFragment(int type, boolean createIfNotExisted) {
        if (type == BASE_TAB_FRAGMENT_DEFAULT_TYPE) {
            return null;
        }

        Fragment fragment = mFragments.get(type);
        if (fragment == null) {
            if (createIfNotExisted) {
                fragment = createFragment(type);
                if (fragment != null) {
                    mFragments.put(type, fragment);
                }
            }
        }

        return fragment;
    }

    private void setTabState(int type) {
        if (mCurrentType == type) {
            return;
        }

        if (mCurrentType != BASE_TAB_FRAGMENT_DEFAULT_TYPE) {
            View tab = mTabs.get(mCurrentType);
            onTabSelected(mCurrentType, tab, false);
        }

        View tab = mTabs.get(type);
        onTabSelected(type, tab, true);

        mCurrentType = type;
    }
}
