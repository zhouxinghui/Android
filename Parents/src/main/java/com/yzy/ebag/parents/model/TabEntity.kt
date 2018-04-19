package com.yzy.ebag.parents.model

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity(private val title: String, private val selectedIcon: Int, private val unSelectedIcon: Int) : CustomTabEntity {

    override fun getTabUnselectedIcon(): Int = unSelectedIcon
    override fun getTabSelectedIcon(): Int = selectedIcon
    override fun getTabTitle(): String = title
}