package com.cai.compress.SCCG.methods;

import com.cai.compress.SCCG.entity.Position;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ORGD {

	public static List<Position> L_list;
	public static List<Position> N_list;
	public static String meta_data;
	public static int length;
	public static int line_length;

	public static String readSeq(String sequenceFileName) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(sequenceFileName);
		DataInputStream datainputstream = new DataInputStream(fileinputstream);
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
		String line;

		StringBuilder stringbuilder = new StringBuilder();
		line = bufferedreader.readLine();
		while ((line = bufferedreader.readLine()) != null) {
			stringbuilder.append(line);
		}
		bufferedreader.close();
		return stringbuilder.toString();
	}

	public static String readrefSeq(String sequenceFileName) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(sequenceFileName);
		DataInputStream datainputstream = new DataInputStream(fileinputstream);
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
		String line;
		int line_length;
		char temp_ch;

		StringBuilder stringbuilder = new StringBuilder();
		line = bufferedreader.readLine();
		while ((line = bufferedreader.readLine()) != null) {
			line_length = line.length();
			for (int i = 0; i < line_length; i++) {
				temp_ch = line.charAt(i);
				if (!Character.isUpperCase(temp_ch)) {
					temp_ch = Character.toUpperCase(temp_ch);
				}
				if (temp_ch != 'N') {
					stringbuilder.append(temp_ch);
				}
			}
		}
		bufferedreader.close();
		return stringbuilder.toString();
	}

	public static void use7zip(String filename, String Dfilename) throws IOException {
		File zipFile = new File(filename);
		if (!zipFile.exists()) {
			return;
		}
		String exec = "7za e " + filename + " -o" + Dfilename + " -aos";
		try {
			Process process = Runtime.getRuntime().exec(exec);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StringBuilder reconstruct(String inFileName, String reference) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(inFileName);
		DataInputStream datainputstream = new DataInputStream(fileinputstream);
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
		String line;
		StringBuilder stringbuilder = new StringBuilder();
		int prev_end = 0, index = 0;

		while ((line = bufferedreader.readLine()) != null) {
			if (index == 4) {
				if (line.contains(",")) {
					String[] strings = line.split(",");
					int begin = Integer.parseInt(strings[0]);
					int end = Integer.parseInt(strings[1]);
					begin += prev_end;
					end += begin;
					try {
						String text = reference.substring(begin, end + 1);
						stringbuilder.append(text);
						prev_end = end;
					} catch (Exception x) {
						System.out.println("come" + prev_end + begin + end);
					}
				} else if (line.length() > 0) {
					stringbuilder.append(line);
				}
				continue;
			} else if (index < 4) {
				index++;
				continue;
			}
		}
		bufferedreader.close();
		return stringbuilder;
	}

	public static void write(String fileName, String text) throws IOException {
		FileWriter filewriter = new FileWriter(fileName, false);
		BufferedWriter out = new BufferedWriter(filewriter);
		out.write(text);
		out.flush();
		out.close();
	}

	public static long getCpuTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
	}

	public static void test(String ref, String tar) {
		for (int i = 0; i < ref.length(); i++) {
			if (ref.charAt(i) != tar.charAt(i)) {
				System.out.println("error:" + i);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Make sure you have inputted 3 arguments.");
			System.exit(0);
		}

		File compress_file = new File(args[1]);

		// final output path
		String file_name = compress_file.getName();

		String[] file_name_parts = file_name.split("_ref_");
		String extractedPart = "";
		if (file_name_parts.length > 0) {
			extractedPart = file_name_parts[0];
		} else {
			System.out.println("Separator not found in the file name.");
		}

		String completeOutputPath = args[2] + "/" + extractedPart;
		Runtime.getRuntime().exec("mkdir " + completeOutputPath);
		String final_file;
		String reference_file;
		String unzip_folder = completeOutputPath + "/unzip";
		Runtime.getRuntime().exec("mkdir " + unzip_folder);
		String output;

		// 7z uncompress
		use7zip(args[1], unzip_folder);

		// 7za解压缩后会在目标文件夹生成一个与文件名相同的空文件夹
		int lastDotIndex = file_name.lastIndexOf('.');
		String file_name_part = "";
		if (lastDotIndex != -1) {
			file_name_part = file_name.substring(0, lastDotIndex);
			System.out.println("Extracted Part: " + file_name_part);
		} else {
			System.out.println("Dot (.) not found in the file name.");
		}
		// String temp_za_extra = file_name_part;
		String za_extra = unzip_folder + "/" + file_name_part;
		Runtime.getRuntime().exec("rm -rf " + za_extra);

		File uzip_path = new File(unzip_folder);
		// get all file_name in compress_file_path
		ArrayList<String> fileNames;
		String[] chrName = null;

		File[] files = uzip_path.listFiles();
		List<File> fileList = new ArrayList<>(Arrays.asList(files));
		for (File file : files) {
			if (file.isDirectory())
				fileList.remove(file);
		}
		File[] updated_files = fileList.toArray(new File[0]);

		fileNames = new ArrayList<>();
		if (updated_files != null) {
			for (File file : updated_files) {
				fileNames.add(file.getName());
			}
			chrName = fileNames.toArray(new String[0]);
		}

		Date startDate = new Date();
		long startCpuTimeNano = getCpuTime();
		System.out.println("Start time: " + startDate);
		System.out.println(args[1] + " is decompressing...");

		for (int i = 0; i < chrName.length; i++) {
			reference_file = args[0] + "/" + chrName[i];
			final_file = unzip_folder + "/" + chrName[i];
			output = args[2] + "/" + chrName[i];

			File file = new File(output);
			file.delete();

			FileInputStream fileinputstream = new FileInputStream(final_file);
			DataInputStream datainputstream = new DataInputStream(fileinputstream);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
			String line;
			int Pindex = 0;

			L_list = new ArrayList<Position>();
			N_list = new ArrayList<Position>();

			// load pre-processing data
			while ((line = bufferedreader.readLine()) != null) {
				if (Pindex == 1) {
					line_length = Integer.parseInt(line);
					Pindex++;
					continue;
				} else if (Pindex == 2) {
					if (line == null || line.length() <= 0) {
						Pindex++;
						continue;
					}
					String[] strings = line.split(" ");
					int j = 0, prev = 0;
					while (j < strings.length) {
						Position position = new Position();
						position.startinRef = prev + Integer.parseInt(strings[j]);
						j++;
						position.endinRef = position.startinRef + Integer.parseInt(strings[j]);
						j++;
						L_list.add(position);
						prev = position.endinRef;
					}
					Pindex++;
					continue;
				} else if (Pindex == 3) {
					if (line == null || line.length() <= 0) {
						Pindex++;
						continue;
					}
					String[] strings = line.split(" ");
					int j = 0, prev = 0;
					while (j < strings.length) {
						Position position = new Position();
						position.startinRef = prev + Integer.parseInt(strings[j]);
						j++;
						position.endinRef = position.startinRef + Integer.parseInt(strings[j]);
						j++;
						N_list.add(position);
						prev = position.endinRef;
					}
					Pindex++;
					continue;
				} else if (Pindex == 0) {
					meta_data = line + "\n";
					Pindex++;
					continue;
				}
			}
			bufferedreader.close();

			String reference = "";
			if (N_list.size() > 0) {
				reference = readrefSeq(reference_file);
			} else if (N_list.size() <= 0) {
				reference = readSeq(reference_file);
				reference = reference.toUpperCase();
			}

			// reconstruct target sequence
			StringBuilder target_string = reconstruct(final_file, reference);

			// complete target
			StringBuilder interim = new StringBuilder();
			int index = 0, iterator = 0;
			boolean accept = false;
			for (Position position : N_list) {
				while (true) {
					if (iterator >= position.startinRef && iterator < position.endinRef) {
						interim.append('N');
					} else if (iterator < position.startinRef && !accept) {
						interim.append(target_string.charAt(index));
						index = index + 1;
						if (index >= target_string.length()) {
							accept = true;
						}
					}
					iterator++;

					if (iterator >= position.endinRef) {
						break;
					}
				}
			}
			if (N_list.size() > 0) {
				while (index < target_string.length()) {
					interim.append(target_string.charAt(index));
					index++;
					iterator++;
				}
			}

			if (interim.length() > 0) {
				target_string = new StringBuilder(interim);
			} else {
			}

			for (Position position : L_list) {
				for (int j = position.startinRef; j <= position.endinRef; j++) {
					if (j == target_string.length()) {
						break;
					}
					target_string.setCharAt(j, Character.toLowerCase(target_string.charAt(j)));
				}
			}

			String final_string = target_string.toString();
			target_string = new StringBuilder();
			target_string.append(final_string.charAt(0));

			for (int t = 1; t < final_string.length(); t++) {
				if (t % line_length == 0) {
					target_string.append("\n");
				}
				target_string.append(final_string.charAt(t));
			}

			final_string = meta_data + (target_string.append("\n")).toString();
			write(output, final_string);
			System.out.println("Decompressed file is " + args[2] + "/" + chrName[i]);
		}

		long taskCpuTimeNano = getCpuTime() - startCpuTimeNano;
		System.out.println("Decompressed time: " + (double) taskCpuTimeNano / 1000000000.0 + " seconds.");
		System.out.println("Done\n" + "----------------------------------------------------------------------------");
		Runtime.getRuntime().exec("rm -rf " + unzip_folder);
	}

	public void uncompress(String reference_path, String compress_file_path, String output_path)
			throws IOException, InterruptedException {
		String[] args = new String[] { reference_path, compress_file_path, output_path };

		int i;
		for (i = 0; i < 3; i++) {
			if (args[i] == "") {
				System.out.println("Make sure you have inputted 3 arguments.");
				System.exit(0);
			}
		}

		File compress_file = new File(compress_file_path);
		String file_name = compress_file.getName();
		String[] file_name_parts = file_name.split("_ref_");
		String extractedPart = "";

		if (file_name_parts.length > 0) {
			extractedPart = file_name_parts[0];
		} else {
			System.out.println("Separator not found in the file name.");
		}

		String completeOutputPath = args[2] + "/" + extractedPart;
		Runtime.getRuntime().exec("mkdir " + completeOutputPath);
		String final_file;
		String reference_file;
		String unzip_folder = completeOutputPath + "/unzip";
		Runtime.getRuntime().exec("mkdir " + unzip_folder);
		String output;

		// 7z uncompress
		use7zip(args[1], unzip_folder);

		// 7za解压缩后会在目标文件夹生成一个与文件名相同的空文件夹
		int lastDotIndex = file_name.lastIndexOf('.');
		String file_name_part = "";
		if (lastDotIndex != -1) {
			file_name_part = file_name.substring(0, lastDotIndex);
			System.out.println("Extracted Part: " + file_name_part);
		} else {
			System.out.println("Dot (.) not found in the file name.");
		}

		// String temp_za_extra = file_name_part;
		String za_extra = unzip_folder + "/" + file_name_part;
		Runtime.getRuntime().exec("rm -rf " + za_extra);

		File uzip_path = new File(unzip_folder);
		// get all file_name in compress_file_path
		ArrayList<String> fileNames;
		String[] chrName = null;

		System.out.println("-------------");

		File[] files = uzip_path.listFiles();
		List<File> fileList = new ArrayList<>(Arrays.asList(files));
		for (File file : files) {
			if (file.isDirectory())
				fileList.remove(file);
		}
		File[] updated_files = fileList.toArray(new File[0]);

		fileNames = new ArrayList<>();
		if (updated_files != null) {
			for (File file : updated_files) {
				fileNames.add(file.getName());
			}
			chrName = fileNames.toArray(new String[0]);
		}

		Date startDate = new Date();
		long startCpuTimeNano = getCpuTime();
		System.out.println("Start time: " + startDate);
		System.out.println(args[1] + " is decompressing...");

		for (i = 0; i < chrName.length; i++) {
			reference_file = args[0] + "/" + chrName[i];
			final_file = unzip_folder + "/" + chrName[i];
			output = args[2] + "/" + chrName[i];

			File file = new File(output);
			file.delete();

			FileInputStream fileinputstream = new FileInputStream(final_file);
			DataInputStream datainputstream = new DataInputStream(fileinputstream);
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
			String line;
			int Pindex = 0;

			L_list = new ArrayList<Position>();
			N_list = new ArrayList<Position>();

			// load pre-processing data
			while ((line = bufferedreader.readLine()) != null) {
				if (Pindex == 1) {
					line_length = Integer.parseInt(line);
					Pindex++;
					continue;
				} else if (Pindex == 2) {
					if (line == null || line.length() <= 0) {
						Pindex++;
						continue;
					}
					String[] strings = line.split(" ");
					int j = 0, prev = 0;
					while (j < strings.length) {
						Position position = new Position();
						position.startinRef = prev + Integer.parseInt(strings[j]);
						j++;
						position.endinRef = position.startinRef + Integer.parseInt(strings[j]);
						j++;
						L_list.add(position);
						prev = position.endinRef;
					}
					Pindex++;
					continue;
				} else if (Pindex == 3) {
					if (line == null || line.length() <= 0) {
						Pindex++;
						continue;
					}
					String[] strings = line.split(" ");
					int j = 0, prev = 0;
					while (j < strings.length) {
						Position position = new Position();
						position.startinRef = prev + Integer.parseInt(strings[j]);
						j++;
						position.endinRef = position.startinRef + Integer.parseInt(strings[j]);
						j++;
						N_list.add(position);
						prev = position.endinRef;
					}
					Pindex++;
					continue;
				} else if (Pindex == 0) {
					meta_data = line + "\n";
					Pindex++;
					continue;
				}
			}
			bufferedreader.close();

			String reference = "";

			if (N_list.size() > 0) {
				reference = readrefSeq(reference_file);
			} else if (N_list.size() <= 0) {
				reference = readSeq(reference_file);
				reference = reference.toUpperCase();
			}

			// reconstruct target sequence
			StringBuilder target_string = reconstruct(final_file, reference);

			// complete target
			StringBuilder interim = new StringBuilder();
			int index = 0, iterator = 0;
			boolean accept = false;
			for (Position position : N_list) {

				while (true) {

					if (iterator >= position.startinRef && iterator < position.endinRef) {
						interim.append('N');
					} else if (iterator < position.startinRef && !accept) {
						interim.append(target_string.charAt(index));
						index = index + 1;
						if (index >= target_string.length()) {
							accept = true;
						}
					}
					iterator++;

					if (iterator >= position.endinRef) {
						break;
					}
				}
			}
			if (N_list.size() > 0) {
				while (index < target_string.length()) {
					interim.append(target_string.charAt(index));
					index++;
					iterator++;
				}
			}

			if (interim.length() > 0) {
				target_string = new StringBuilder(interim);
				// System.out.println("FINAL SIZE: " + target_string.length());
			} else {
				// System.out.println("FINAL SIZE: " + target_string.length());
			}

			for (Position position : L_list) {
				for (int j = position.startinRef; j <= position.endinRef; j++) {
					if (j == target_string.length()) {
						break;
					}
					target_string.setCharAt(j, Character.toLowerCase(target_string.charAt(j)));
				}
			}

			String final_string = target_string.toString();

			target_string = new StringBuilder();
			target_string.append(final_string.charAt(0));

			for (int t = 1; t < final_string.length(); t++) {
				if (t % line_length == 0) {
					target_string.append("\n");
				}
				target_string.append(final_string.charAt(t));
			}

			final_string = meta_data + (target_string.append("\n")).toString();

			write(output, final_string);

			System.out.println("Decompressed file is " + args[2] + "/" + chrName[i]);
		}

		long taskCpuTimeNano = getCpuTime() - startCpuTimeNano;
		System.out.println("Decompressed time: " + (double) taskCpuTimeNano / 1000000000.0 + " seconds.");
		System.out.println("Done\n" + "----------------------------------------------------------------------------");
		Runtime.getRuntime().exec("rm -rf " + unzip_folder);
	}

}
