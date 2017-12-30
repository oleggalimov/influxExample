import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;


public class client {
    private static final String influxHost = "http://192.168.56.101:8086/";
    private static final String influxUser = "root";
    private static final String influxPass = "rootf";
    private static final String dataBase = "testDB";
    private static final String retentionPolicy = "autogen";



    public static void main(String[] args) {
        try  {
            //коннектимся
            InfluxDB influxDB = InfluxDBFactory.connect(influxHost,influxUser, influxPass);
            //создаем бд
            if (!influxDB.databaseExists(dataBase)) {
                influxDB.createDatabase(dataBase);
                //проверяем чир бд существует - смотрим список баз
                System.out.println(influxDB.describeDatabases());
                //создаем батчинг - собирать 20 точек не меньше 100ms
                influxDB.enableBatch(20, 100, TimeUnit.MILLISECONDS);
                //проверяем что батчинг есть
                System.out.println(influxDB.isBatchEnabled());
            }

            Point cpuPoint, diskUsage;
            for (;;) {
                cpuPoint = Point.measurement("cpu").addField("usage", Math.random()).build();
                diskUsage = Point.measurement("disk").addField("usage", Math.random()).build();
                influxDB.write(dataBase,retentionPolicy, cpuPoint);
                influxDB.write(dataBase,retentionPolicy, diskUsage);

            }
        } catch (InfluxDBException iexb) {
            iexb.printStackTrace();
        } finally {
            System.out.println("Выполнено");
        }


    }
}
