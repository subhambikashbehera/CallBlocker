package com.subhamassignment.callblocker.Utils

import java.lang.ref.WeakReference
import java.util.*

open class BlacklistObserver {


    protected val observers: MutableList<WeakReference<Observer?>> = LinkedList()

    fun addObserver(observer: Observer, immediate: Boolean) {
        observers.add(WeakReference(observer))
        if (immediate) observer.onBlacklistUpdate()
    }

//    open fun removeObserver(observer: Observer) {
//        for (ref in observers){
//            if (ref.get() === observer){
//                observers.remove(observer)
//            }
//        }
//    }

    fun notifyUpdated() {
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val ref = iterator.next()
            if (ref.get() != null) ref.get()!!.onBlacklistUpdate() else  //observers.remove(ref);
                iterator.remove()
        }
    }


    interface Observer {
        fun onBlacklistUpdate()
    }

}