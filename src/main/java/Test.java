import Entity.MyCookie;
import util.MiniToolUtil;
import util.ThreadTest;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static util.ThreadTest.COUNT;
import static util.VarUtil.*;


public class Test {


    public static void main(String[] args) throws IOException, InterruptedException {
        COOKIES_LIST = new ArrayList<>();
        COUNT = 0;

        HOST_URL = "http://10.128.13.45:6656/";
        loop:
        while (true) {
            COUNT_DONE = 0;
            COUNT_PEND = 0;
            System.out.println("==========================================");
            System.out.println("Nhập lựa chọn");
            System.out.println("1.Cookie mới và lặp");
            System.out.println("2.Cookie cũ và lặp");
            System.out.println("4.Chạy với cookie cũ và tùy chọn số luồng");
            System.out.println("5.Chạy và tạo cookie mới với tùy chọn số luồng");
            System.out.println("0.Config");
            System.out.println("==========================================");
            System.out.print("Lựa chọn: ");
            Scanner scanner = new Scanner(System.in);
            int a = scanner.nextInt();


            main:
            switch (a) {
                case 1:
                    System.out.println("Số lần muốn lặp:");
                    int b = scanner.nextInt();
                    loopB(b);
                    break;

                case 2:
                    System.out.println("Lặp lại cookie cũ");
                    loopB(readCookies());

                    break;
                case 0:
                    System.out.println("1. Sửa Host");
                    System.out.println("2. Loại thiết bị");
                    int chose = scanner.nextInt();
                    switch (chose) {
                        case 1:
                            System.out.println("Nhập host mới");
//                    scanner.nextLine();
                            String newHost = scanner.next();
                            HOST_URL = newHost;
                            System.out.println("Host mới: " + newHost);
                            break main;
                        case 2:
                            System.out.println("1.Máy tính");
                            System.out.println("2. Điện thoại");
                            chose = scanner.nextInt();
                            switch (chose) {
                                case 1:
                                    System.out.println("Thiết bị hiện tại là: Máy tính");
                                    IS_MOBILE = false;
                                    break main;
                                case 2:
                                    System.out.println("Thiết bị hiện tại là: Điện thoại");
                                    IS_MOBILE = true;
                                    break main;
                                default:
                                    break;
                            }
                        default:
                            break;
                    }


                case 4:
                    System.out.println("Advance");
                    System.out.println("Input your thread you want to run");
                    int numbTh = scanner.nextInt();
                    System.out.println("Using Cookie File to Run");

                    COOKIES_LIST.clear();
                    COOKIES_LIST.addAll(readCookies());

                    for (int i = 0; i < numbTh; i++) {
                        ThreadTest threadTest = new ThreadTest(false);
                        threadTest.start();
                    }
                    while (COUNT_DONE != COOKIES_SIZE) {
                        Thread.sleep(500);
                    }
                    System.out.println("\nDONE!!!");
                    break;
                case 5:
                    System.out.println("Số cookie muốn tạo mới");
                    int cn = scanner.nextInt();
                    System.out.println("Số luồng muốn chạy");
                    int tn = scanner.nextInt();
                    if (cn < tn) {
                        tn = cn;
                    }
                    COOKIES_SIZE = cn;
                    for (int i = 0; i < tn; i++) {
                        ThreadTest threadTest = new ThreadTest("RWT");
                        threadTest.start();
                    }
                    while (COUNT_DONE != COOKIES_SIZE) {
                        Thread.sleep(500);
                    }
                    COOKIES_SIZE = 0;
                    saveCookie(COOKIES_LIST,true);
                    System.out.println("\nDONE!!!");
                    scanner.nextLine();
                    break;
                case 6:
                    for (int i = 0; i < 100; i++) {
                        MiniToolUtil.getInstance().BarProgress(i + 1, 100);
                    }
                case 7:
                    List<MyCookie> myCookies = readCookies();
                    List<String> strings = myCookies.stream().map(MyCookie::getGa).collect(Collectors.toList());
                    List<MyCookie> strings2 = new ArrayList<>();
                    for (MyCookie myCookie : myCookies
                    ) {
                        if(!strings2.stream().map(MyCookie::getGid).collect(Collectors.toList()).contains(myCookie.getGid())){
                            strings2.add(myCookie);
                        }
                    }
                    saveCookie(strings2,false);

                    System.out.println("OK");
                    System.out.println("Before: " + myCookies.size());
                    System.out.println("After: " + strings2.size());
                    break ;
                default:
                    System.out.println("Đang phát triển!");
                    break loop;
            }
        }


    }

    public static void loopB(int loopTime) throws InterruptedException, IOException {

        System.out.println("Begin Thread");

        if (loopTime > 5) {
            for (int i = 0; i < 5; i++) {
                ThreadTest threadTest = new ThreadTest(i);
                threadTest.start();
            }
            while (COUNT != 5) {
                Thread.sleep(500);
            }
        } else {

            for (int i = 0; i < loopTime; i++) {
                ThreadTest threadTest = new ThreadTest(i);
                threadTest.start();
            }
            while (COUNT != loopTime) {

                Thread.sleep(500);
            }
        }

        COUNT = 0;
        saveCookie(COOKIES_LIST,true);
        COOKIES_LIST.clear();

        if (loopTime > 5) {
            loopB(loopTime - 5);
        }

    }

    public static void loopB(List<MyCookie> cookies) throws InterruptedException {

        System.out.println("Begin Thread....");
        if (cookies.size() > 5) {
            for (int i = 0; i < 5; i++) {
                ThreadTest threadTest = new ThreadTest(cookies.get(0));
                threadTest.start();
                cookies.remove(0);
            }
            while (COUNT != 5) {
                Thread.sleep(500);
            }
        } else {
            int originCookieSize = cookies.size();
            for (int i = 0; i < cookies.size(); ) {
                ThreadTest threadTest = new ThreadTest(cookies.get(0));
                threadTest.start();
                cookies.remove(0);
            }
            while (COUNT != originCookieSize) {

                Thread.sleep(500);
            }
        }


        COUNT = 0;


        if (cookies.size() > 5) {

            loopB(cookies);
        } else {
            System.out.println("Ấn nút Enter để tiếp tục");
            Scanner scc = new Scanner(System.in);
            scc.nextLine();
        }

    }

    public static void saveCookie(List<MyCookie> listCookies,boolean isAppend) throws IOException {

        File myObj;
        FileWriter myWriter = null;

        try {

            myObj = new File("filename.txt");
            myObj.createNewFile();
            myWriter = new FileWriter(myObj, isAppend);
            for (MyCookie a : listCookies) {
                myWriter.append(a.getSid());
                myWriter.append("_");
                myWriter.append(a.getGid());
                myWriter.append("_");
                myWriter.append(a.getGa());
                myWriter.append(",");
            }
            System.out.println("\nHave: " + listCookies.size() + " Cookie is Added");
        } catch (Exception ignored) {
        } finally {

            if (myWriter != null) {
                myWriter.close();

            }
        }
    }

    public static List<MyCookie> readCookies() throws IOException {
        COOKIES_SIZE = 0;
        File myObj;
        FileReader myReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            myObj = new File("filename.txt");
            myReader = new FileReader(myObj);
            int i;
            while ((i = myReader.read()) != -1) {
//                System.out.print((char) i);
                sb.append((char) i);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (myReader != null) {
                myReader.close();
            }

        }
        List<String> cookies = new ArrayList<>(Arrays.asList(sb.toString().split(",")));
        List<MyCookie> myCookies = new ArrayList<>();
        for (String cookieRaw : cookies
        ) {
            MyCookie myCookie = new MyCookie(
                    cookieRaw.split("_")[0],
                    cookieRaw.split("_")[1],
                    cookieRaw.split("_")[2]
            );
            myCookies.add(myCookie);
        }

        //        cookies.remove(cookies.size() - 1);
        COOKIES_SIZE = myCookies.size();
        System.out.println("We have: " + myCookies.size() + " Cookies");
        return myCookies;
    }
}
