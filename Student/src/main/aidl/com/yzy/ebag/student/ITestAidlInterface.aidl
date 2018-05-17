// ITestAidlInterface.aidl
package com.yzy.ebag.student;

// Declare any non-default types here with import statements
import com.yzy.ebag.student.IParticipateCallback;
interface ITestAidlInterface {
    void start(IParticipateCallback cb, int time);
        void unregisterParticipateCallback(IParticipateCallback cb);
}
