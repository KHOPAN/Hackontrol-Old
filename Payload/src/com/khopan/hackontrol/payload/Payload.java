package com.khopan.hackontrol.payload;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Payload {
	// https://stackoverflow.com/a/23538961/17136195
	public static boolean isRunningAsAdministrator() {
		Preferences preferences = Preferences.systemRoot();

		synchronized(System.err) {
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(int data) {

				}
			}));

			try {
				preferences.put("foo", "bar"); // SecurityException on Windows
				preferences.remove("foo");
				preferences.flush(); // BackingStoreException on Linux
				return true;
			} catch(Exception exception) {
				return false;
			} finally {
				System.setErr(System.err);
			}
		}
	}

	public static void main(String[] args) {
		if(!Payload.isRunningAsAdministrator()) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Throwable ignored) {

			}

			JOptionPane.showMessageDialog(null, "Hackontrol Payload requires an Administrator Privileges!", "Payload Error", JOptionPane.ERROR_MESSAGE);
			Runtime.getRuntime().exit(1);
			return;
		}
	}
}
