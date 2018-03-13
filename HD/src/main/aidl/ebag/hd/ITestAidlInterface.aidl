// ITestAidlInterface.aidl
package ebag.hd;

// Declare any non-default types here with import statements
import ebag.hd.IParticipateCallback;
interface ITestAidlInterface {
    void start(IParticipateCallback cb, int time);
    void unregisterParticipateCallback(IParticipateCallback cb);
}
