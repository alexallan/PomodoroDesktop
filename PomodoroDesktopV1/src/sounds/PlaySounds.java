package sounds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySounds {

	/** Plays a sound to indicate a notifcation */
	public static void playNotifySound() {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {

				try {

					String filePath = new File("").getAbsolutePath();
					filePath += "\\sounds\\Notify.wav";
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(new BufferedInputStream(
									new FileInputStream(filePath)));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();

	}

	/** Plays an annoying alarm sound*/
	public static void playAlarmSound() {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {

				try {

					String filePath = new File("").getAbsolutePath();
					filePath += "\\sounds\\Alarm.wav";
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(new BufferedInputStream(
									new FileInputStream(filePath)));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}

		}).start();

	}

	/** Plays a ticky sound */
	public static void playTickSound() {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {

				try {

					String filePath = new File("").getAbsolutePath();
					filePath += "\\sounds\\TickTock.wav";
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(new BufferedInputStream(
									new FileInputStream(filePath)));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

}
