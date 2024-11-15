public class main {
    public static void main(String[] args) {
        String valeur = "14";
        if (valeur.matches("-?\\d+(\\.\\d+)?")){
            System.out.println(valeur);
        }
    }
}
