import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ClassSkeletonGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the class name: ");
        String className = scanner.next();

        System.out.print("Enter the number of fields: ");
        int numFields = scanner.nextInt();

        // Create class skeleton
        String classSkeleton = generateClassSkeleton(className, numFields, scanner);
        String classFolder = className.toLowerCase();
        createDirectory(classFolder);
        writeToFile(classFolder + "/" + className, classSkeleton);

        // Create DAO interface skeleton
        String daoSkeleton = generateDAOSkeleton(className);
        String daoFolder = classFolder + "/dao";
        createDirectory(daoFolder);
        writeToFile(daoFolder + "/" + className + "DAO", daoSkeleton);

        // Create DAOImpl class skeleton
        String daoImplSkeleton = generateDAOImplSkeleton(className);
        String daoImplFolder = daoFolder + "/impl";
        createDirectory(daoImplFolder);
        writeToFile(daoImplFolder + "/" + className + "DAOImpl", daoImplSkeleton);

        // Create Service interface skeleton
        String serviceSkeleton = generateServiceSkeleton(className);
        String serviceFolder = classFolder + "/service";
        createDirectory(serviceFolder);
        writeToFile(serviceFolder + "/" + className + "Service", serviceSkeleton);

        // Create ServiceImpl class skeleton
        String serviceImplSkeleton = generateServiceImplSkeleton(className);
        String serviceImplFolder = serviceFolder + "/impl";
        createDirectory(serviceImplFolder);
        writeToFile(serviceImplFolder + "/" + className + "ServiceImpl", serviceImplSkeleton);

        // Create ServiceFactory abstract class skeleton
        String serviceFactorySkeleton = generateServiceFactorySkeleton(className);
        String serviceFactoryFolder = classFolder + "/factory";
        createDirectory(serviceFactoryFolder);
        writeToFile(serviceFactoryFolder + "/" + className + "ServiceFactory", serviceFactorySkeleton);

        // Create ServiceFactoryImpl class skeleton
        String serviceFactoryImplSkeleton = generateServiceFactoryImplSkeleton(className);
        writeToFile(serviceFactoryFolder + "/" + className + "ServiceFactoryImpl", serviceFactoryImplSkeleton);

        scanner.close();
    }

    private static String generateClassSkeleton(String className, int numFields, Scanner scanner) {
        StringBuilder code = new StringBuilder();

        // Class declaration
        code.append("public class ").append(className).append(" extends DSpaceObject implements DSpaceObjectLegacySupport {\n\n");

        // Fields
        for (int i = 1; i <= numFields; i++) {
            System.out.print("Field " + i + " datatype: ");
            String datatype = scanner.next();

            System.out.print("Field " + i + " name: ");
            String fieldName = scanner.next();

            code.append("\tprivate ").append(datatype).append(" ").append(fieldName).append(";\n");
        }

        // Class closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateDAOSkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Interface declaration
        code.append("public interface ").append(className).append("DAO")
            .append(" extends DSpaceObjectDAO<").append(className).append(">, DSpaceObjectLegacySupportDAO<").append(className).append("> {\n\n");
        
        // Interface closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateDAOImplSkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Class declaration
        code.append("public class ").append(className).append("DAOImpl")
            .append(" extends AbstractHibernateDSODAO<").append(className).append(">")
            .append(" implements ").append(className).append("DAO {\n\n");

        // Class closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateServiceSkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Interface declaration
        code.append("public interface ").append(className).append("Service")
            .append(" extends DSpaceObjectService<").append(className).append(">, DSpaceObjectLegacySupportService<").append(className).append("> {\n\n");
        
        // Interface closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateServiceImplSkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Class declaration
        code.append("public class ").append(className).append("ServiceImpl")
            .append(" extends DSpaceObjectServiceImpl<").append(className).append(">")
            .append(" implements ").append(className).append("Service {\n\n");

        // Class closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateServiceFactorySkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Abstract class declaration
        code.append("public abstract class ").append(className).append("ServiceFactory {\n\n");

        // Abstract method
        code.append("\tpublic abstract ").append(className).append("Service get").append(className).append("Service();\n\n");

        // Static method
        code.append("\tpublic static ").append(className).append("ServiceFactory getInstance() {\n");
        code.append("\t\treturn DSpaceServicesFactory.getInstance().getServiceManager().getServiceByName(\"")
                .append(className.toLowerCase()).append("ServiceFactory\", ").append(className).append("ServiceFactory.class);\n");
        code.append("\t}\n");

        // Class closing
        code.append("}\n");

        return code.toString();
    }

    private static String generateServiceFactoryImplSkeleton(String className) {
        StringBuilder code = new StringBuilder();

        // Class declaration
        code.append("public class ").append(className).append("ServiceFactoryImpl")
            .append(" extends ").append(className).append("ServiceFactory").append(" {\n\n");

        // Autowired field
        code.append("\t@Autowired\n");
        code.append("\t").append(className).append("Service ").append(className.toLowerCase()).append("Service;\n\n");

        // Abstract method implementation
        code.append("\t@Override\n");
        code.append("\tpublic ").append(className).append("Service get").append(className).append("Service() {\n");
        code.append("\t\treturn ").append(className.toLowerCase()).append("Service;\n");
        code.append("\t}\n");

        // Class closing
        code.append("}\n");

        return code.toString();
    }

    private static void createDirectory(String folderName) {
        File directory = new File(folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static void writeToFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName + ".java");
            fileWriter.write(content);
            fileWriter.close();
            System.out.println("\n" + fileName + ".java generated successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
