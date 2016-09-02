package it.miriade.commons.utils;

public class OSValidator {

	private static String os = StringHandler.toString(System.getProperty("os.name")).toLowerCase();

	public enum OS {
		WIN("Windows"), MAC("Mac"), UNIX("Unix or Linux"), SOLARIS("Solaris"), UNKNOW("Unknow");
		private String fullname;

		private OS(String fullname) {
			this.fullname = fullname;
		}

		public static OS whatAmI() {
			return OSValidator.getOS();
		}

		@Override
		public String toString() {
			return this.fullname;
		}

	}

	public static OS getOS() {
		return getOS(os);
	}

	public static OS getOS(String os) {
		if (isWindows(os))
			return OS.WIN;
		else if (isMac(os))
			return OS.MAC;
		else if (isUnix(os))
			return OS.UNIX;
		else if (isSolaris(os))
			return OS.SOLARIS;
		else
			return OS.UNKNOW;
	}

	public static boolean isWindows() {
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (os.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (os.indexOf("sunos") >= 0);
	}

	public static boolean isWindows(String os) {
		return (StringHandler.toString(os).indexOf("win") >= 0);
	}

	public static boolean isMac(String os) {
		return (StringHandler.toString(os).indexOf("mac") >= 0);
	}

	public static boolean isUnix(String os) {
		String tmp = StringHandler.toString(os);
		return (tmp.indexOf("nix") >= 0 || tmp.indexOf("nux") >= 0 || tmp.indexOf("aix") > 0);
	}

	public static boolean isSolaris(String os) {
		return (StringHandler.toString(os).indexOf("sunos") >= 0);
	}

}