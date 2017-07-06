package com.gome.iam.common.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class CSVUtil {
    private static final String FILE_ENCODE = "GBK";

    public static void download(List<Map> exportData, Map rowMapper,
                                OutputStream outputStream) {
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    outputStream, FILE_ENCODE), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator
                    .hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream.write("\t" + propertyEntry.getValue()
                        + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            for (Map map : exportData) {
                for (Object o : rowMapper.entrySet()) {
                    Map.Entry propertyEntry = (Map.Entry) o;
                    csvFileOutputStream.write("\t"
                            + map.get(propertyEntry.getValue()) + "");
                    csvFileOutputStream.write(",");
                }
                csvFileOutputStream.newLine();
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File createCSVFile(List exportData, LinkedHashMap rowMapper,
                                      String outPutPath, String filename) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFile = new File(outPutPath + filename + ".csv");
            // csvFile.getParentFile().mkdir();
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), FILE_ENCODE), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator
                    .hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream.write(/*"\t"
                        +*/ propertyEntry.getValue() + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();

            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                // Object row = (Object) iterator.next();
                LinkedHashMap row = (LinkedHashMap) iterator.next();
                System.out.println(row);

                for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator
                        .hasNext(); ) {
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator
                            .next();
                    csvFileOutputStream.write(/*"\t"
                            +*/ propertyEntry.getValue() + "");
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 导入
     *
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String[]> importCSV(File file) {
        List<String[]> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(in, FILE_ENCODE));
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    /**
     * 导入
     *
     * @return
     */
    public static List<Map<String, String>> importCSV(InputStream inputStream, String[] title) {
        List<Map<String, String>> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, FILE_ENCODE));
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                if (lineNum != 1 && null != title) {
                    String[] value = line.split(",", title.length);
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < title.length; i++) {
                        map.put(title[i], value[i]);
                    }
                    dataList.add(map);
                }
                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    /**
     * 导入
     *
     * @return
     */
    public static <T> List<T> importCSV(File file, Class<T> c) {
        List<T> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(in, FILE_ENCODE));
            String line;
            while ((line = br.readLine()) != null) {
                T t = c.newInstance();
                Field[] fs = t.getClass().getDeclaredFields();
                String[] valueArray = line.split(",", t.getClass().getDeclaredFields().length);
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    f.setAccessible(true);
                    String type = f.getType().getName();
                    if (type.endsWith("String")) {
                        f.set(t, valueArray[i]);
                    } else if (type.endsWith("int") || type.endsWith("Integer")) {
                        f.set(t, Integer.valueOf(valueArray[i]));
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        f.set(t, Long.valueOf(valueArray[i]));
                    } else if (type.endsWith("float") || type.endsWith("Float")) {
                        f.set(t, Float.valueOf(valueArray[i]));
                    } else if (type.endsWith("double") || type.endsWith("Double")) {
                        f.set(t, Double.valueOf(valueArray[i]));
                    }

                }
                dataList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        List exportData = new ArrayList<Map>();
        Map row1 = new LinkedHashMap<String, String>();
        row1.put("1", "11");
        row1.put("2", "12");
        row1.put("3", "13");
        row1.put("4", "14");
        exportData.add(row1);
        row1 = new LinkedHashMap<String, String>();
        row1.put("1", "21111111111111");
        row1.put("2", "22");
        row1.put("3", "23");
        row1.put("4", "24");
        int i = 0;
        while (i < 50) {
            exportData.add(row1);
            i++;
        }
        List propertyNames = new ArrayList();
        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");
        CSVUtil.createCSVFile(exportData, map, "d:/", "12");
        Long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
        File file = new File("d:/12.csv");
        try {
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
