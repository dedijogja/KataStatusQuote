#include <jni.h>
#include <string>
#include <string.h>

//key text sudah diganti Sutini Dev Lab Update
std::string keyDesText = "d{{KuG6C&9H5:$z9";

//key asset sudah diganti Sutini Dev Lab Update
std::string keyDesAssets = "lOIQWYWrp/KScIjyHE4vWA==";

//kode iklan sudah diganti ke Sutini Dev Lab Update
std::string adBanner =              "CGEA[{_[I<Q!%+_#4-$%-+&2))0`-$55+*@W)!";
std::string adInterstitial =        "CGEA[{_[I<Q!%+_#4-$%-+&2))0`6)--Q*#)Q)";

//startApp sudah diganti ke Sutini Dev Lab Update
std::string startAppId = "@W_(3&@6$";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "com.sut";
std::string tengahP = "inidevla";
std::string ahirP = "b.katalucubahasajawalengkap";
std::string finalPackage = awalP + tengahP + ahirP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "JNI Nya Bro!");
        return NULL;
    }
    return text;
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_adInterstitial(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adInterstitial.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_adBanner(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adBanner.c_str()));
}


extern "C"
JNIEXPORT jstring JNICALL Java_com_sutinidevlab_help_MuahNative_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

