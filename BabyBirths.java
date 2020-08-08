import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
/**
 * Write a description of BabyBirths here.
 * 
 * @author Himanshu Singh 
 * @version 08-08-2020
 */

public class BabyBirths {
    public void totalBirths(FileResource fr){
        int totalBirth = 0;
        int totalGirls = 0;
        int totalBoys = 0;
        int totalNames = 0;
        int totalGirlsNames = 0;
        int totalBoysNames = 0;
        // false becausOf no header in file
        for(CSVRecord currentRow : fr.getCSVParser(false)){
            int birth = Integer.parseInt(currentRow.get(2));
            totalBirth += birth;
            totalNames += 1;
            if(currentRow.get(1).equals("M")){
                totalBoys += birth;
                totalBoysNames += 1;
            }else{
                totalGirls += birth;
                totalGirlsNames += 1;
            }
        }
        System.out.println("Total number of Births : " + totalBirth);
        System.out.println("Total Names : " + totalNames);
        System.out.println("Total number of Boys Births : " + totalBoys);
        System.out.println("Total Boys Names : " + totalBoysNames);
        System.out.println("Total number of Girsl Births : " + totalGirls);
        System.out.println("Total Girls Names : " + totalGirlsNames);
    }
    
    public int getRank(CSVParser parser, String name, String gender){
        int rank = 0;
        boolean present = false;
        for(CSVRecord currentRow : parser){
            String currentGender = currentRow.get(1);
            if(currentGender.equals(gender)){
                rank += 1;
                if(currentRow.get(0).equals(name)){
                    present = true;
                    break;
                }
            }
        }
        if(!present){
            return -1;
        }
        return rank;
    }
    
    public void testTotalBirth(){
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    public void testGetRank(){
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            String name = "Frank";
            String gender = "M";
            int year = getCurrentYear(f);
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            int rank = getRank(parser, name, gender);
            System.out.println("Name " + name + " of gender " + gender + " present in year " + year + " at " + rank);
            break;
        }
    }
    
    public String getName(CSVParser parser, int rank, String gender){
        int tempRank = 0;
        for(CSVRecord currentRow : parser){
            String currentGender = currentRow.get(1);
            if(currentGender.equals(gender)){
                tempRank += 1;
                if(tempRank == rank){
                    return currentRow.get(0);
                }
            }
        }
        return "NO NAME";
    }
    
    public void testGetName(){
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            int rank = 450;
            String gender = "F";
            int year = getCurrentYear(f);
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            String resName = getName(parser, rank, gender);
            System.out.println("Name of gender " + gender + " having rank " + rank + " in year " + year + " is " + resName);
            break;    
        }
    }
    
    public void whatIsNameInYear(CSVParser parser1, CSVParser parser2, String name, int year, int newYear, String gender){
        int rank = getRank(parser1, name, gender);
        String resName = getName(parser2, rank, gender);
        System.out.println(name + " born in " + year + " would be " + resName + " if she was born in " + newYear + ".");
    }
    
    public void testWhatIsNameInYear(){
        File f1 = new File("us_babynames_by_year/yob1972.csv");
        FileResource fr = new FileResource(f1);
        CSVParser parser1 = fr.getCSVParser(false);
        String name = "Susan";
        int year = getCurrentYear(f1);
        File f2 = new File("us_babynames_by_year/yob2014.csv");
        fr = new FileResource(f2);
        CSVParser parser2 = fr.getCSVParser(false);
        int newYear = getCurrentYear(f2);
        String gender = "F";
        whatIsNameInYear(parser1, parser2, name, year, newYear, gender);
    }
    
    public int yearOfHighestRank(String name, String gender){
        int year = 0;
        int highestRank = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            int currYear = getCurrentYear(f);
            int currRank = getRank(parser, name, gender);
            if(highestRank == 0 && currRank != -1){
                highestRank = currRank;
                year = currYear;
            }else if(highestRank > currRank && currRank != -1){
                highestRank = currRank;
                year = currYear;
            }
        }
        return year;
    }
    
    public int getCurrentYear(File f){
            String filename = f.getName();
            filename = filename.replaceAll("[^\\d]", "");
            int currYear = Integer.parseInt(filename);
            return currYear;
    }
    
    public void testYearOfHighestRank(){
        int year = yearOfHighestRank("Mich","M");
        System.out.println(year);
    }
    
    public double getAverageRank(String name, String gender){
        double sum = 0;
        int count = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            int currRank = getRank(parser, name, gender);
            if(currRank != -1){
                sum += currRank;
                count++;
            }
        }
        if(count == 0){
            return -1;
        }
        return (sum/count);
    }
    
    public void testAverageRank(){
        double average = getAverageRank("Susan","F");
        System.out.println(average);
    }
    
    public int getTotalBirthsRankedHigher(CSVParser parser, int year, String name, String gender){
        int sum = 0;
        for(CSVRecord record : parser){
            int currRank = 0;
            if(record.get(1).equals(gender) && record.get(0).equals(name)){
                return sum;
            }else if(record.get(1).equals(gender)){
                sum += Integer.parseInt(record.get(2));
            }
        }
        return sum;
    }
    
    public void testTotalBirthsRankedHigher(){
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            int year = getCurrentYear(f);
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            int highestRanked = getTotalBirthsRankedHigher(parser, year, "Emily", "F");
            System.out.println(highestRanked);
            break;
        }
    }
}
