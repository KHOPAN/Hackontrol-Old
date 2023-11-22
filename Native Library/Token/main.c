#include <jni.h>
#include "com_khopan_hackontrol_Hackontrol.h"
#include "token.h"

JNIEXPORT jstring JNICALL Java_com_khopan_hackontrol_Hackontrol_getBotToken(JNIEnv* environment, jclass hackontrolClass) {
	jstring token = (*environment)->NewStringUTF(environment, TOKEN);
	return token;
}
