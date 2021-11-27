import java.util.LinkedList;
import java.util.Scanner;

/*
Problema 1 – Vestuário

As roupas se dividem em categorias, sendo essas:
    social
    esporte
    esporte fino
    trabalho

Todas as roupas possuem características como:
    cor
    número do manequim
    tecido com o qual é feito
    preço

As roupas podem se encontrar em 3 (três) situações (status) distintas:
    no guarda-roupas
    em uso
    na lavanderia

O sistema deve:
    nos dizer quantas roupas estão na lavanderia
    nos dizer quantas roupas estão no guarda-roupas
    nos dizer qual a cor predominante no guarda-roupas
    adicionar e remover roupas
*/

public class Vestuario {
    private static final Scanner scan = new Scanner(System.in);
    
    // coluna (caracteres):     para printf, no método remove_roupa
    // idx (2)   Categoria (12)   Cor (8)   Manequim (5)   Tecido (9)   Preco (8)   Situacao (13)
    private static final String borda  = "+----+--------------+----------+-------+-----------+----------+---------------+\n";
    private static final String titulos = "| %2s | %12s | %8s | %5s | %9s | %8s | %13s |\n";  // printf
    private static final String colunas = "| %2d | %12s | %8s | %5d | %9s | %8.2f | %13s |\n";  // printf

    private static LinkedList<Roupa> roupas = new LinkedList<Roupa>();

    private static void help() {
        System.out.println("===  VESTUÁRIO  ===");
        System.out.println("\tHELP    exibe esta mensagem;");
        System.out.println("\tADD     adiciona nova peça de roupa;");
        System.out.println("\tRM      remove uma peça de roupa da lista;");
        System.out.println("\tCOR     retorna a cor predominante no guarda-roupas;");
        System.out.println("\tLAV     retorna a quantidade de peças na lavanderia;");
        System.out.println("\tGR      retorna a quantidade de peças no guarda-roupas;");
        System.out.println("\tEXIT    encerra o programa;");
        System.out.println("");
    }

    private static <T> T selecionar(T[] opcoes) {
        int sup = opcoes.length;
        for (int i = 0;  i < sup;  i++) {
            System.out.printf("%2d    ", i);
            System.out.println(opcoes[i]);
        }
        int idx;
        do {
            idx = scan.nextInt();
            if ((idx >= 0) && (idx < sup)) {
                break;
            }
            System.out.printf("Valor inválido. Digite um inteiro entre 0 e %d, inclusive.\n", sup - 1);
        } while (true);
        return opcoes[idx];
    }
    private static void adicionar_roupa() {
        // define categoria
        System.out.printf("Qual a categoria da peça? [0..%d]\n", Categoria.values().length - 1);
        Categoria cat = selecionar(Categoria.values());
        System.out.println();

        // define cor
        System.out.printf("Qual a cor da peça? [0..%d]\n", Cor.values().length - 1);
        Cor cor = selecionar(Cor.values());
        System.out.println();

        // define numero do manequim
        System.out.print("Qual o numero do manequim? ");
        int num = scan.nextInt();
        System.out.println();

        // define tecido
        System.out.printf("Qual o tecido da peça? [0..%d]\n", Tecido.values().length - 1);
        Tecido tec = selecionar(Tecido.values());
        System.out.println();

        // define preco
        System.out.print("Qual o preco da peça? ");
        double preco = scan.nextDouble();
        System.out.println();

        // define cor
        System.out.printf("Qual a situacao da peça? [0..%d]\n", Situacao.values().length - 1);
        Situacao sit = selecionar(Situacao.values());

        Roupa roupa = new Roupa(cat, cor, num, tec, preco, sit);
        roupas.add(roupa);
    }

    private static void remover_roupa() {
        // coluna (caracteres):
        // idx (2)   Categoria (12)   Cor (8)   Manequim (5)   Tecido (9)   Preco (8)   Situacao (13)
        System.out.printf(borda);
        System.out.printf(titulos, "id", "Categoria", "Cor", "Maneq", "Tecido", "Preco", "Situacao");
        System.out.printf(borda);
        int idx = 0;
        for (Roupa r : roupas) {
            System.out.printf(colunas, idx, r.categoria, r.cor, r.manequim, r.tecido, r.preco, r.situacao);
            idx++;
        }
        System.out.printf(borda);

        int sup = idx;
        System.out.printf("\nQual roupa deve ser excluída? [0..%d], [[-1] para cancelar]\n", sup - 1);
        do {
            idx = scan.nextInt();
            if ((idx >= 0) && (idx < sup)) {
                break;
            } else if (idx == -1) {
                return;
            }
            System.out.printf("Valor inválido. Digite um inteiro entre 0 e %d, inclusive, ou -1 para cancelar.\n", sup - 1);
        } while (true);

        roupas.remove(idx);
    }

    private static void cor_predominante_gr() {
        Cor[] cores = Cor.values();
        int N = cores.length;
        int[] conts = new int[N];
        for (int i = 0;  i < N;  i++) {
            conts[i] = 0;
        }

        int idx_predominante = 0;
        int maior_cont = 0;

        for (Roupa r : roupas) {
            if (r.situacao != Situacao.GUARDA_ROUPAS) {
                continue;
            }
            int idx = r.cor.getIndex();
            conts[idx] += 1;

            if (conts[idx] > maior_cont) {
                idx_predominante = idx;
                maior_cont = conts[idx];
            }
        }

        System.out.printf("A cor predominante no guarda-roupa é %s. (Com %d peças.)\n", cores[idx_predominante], maior_cont);
    }

    private static void contar_roupas_em(Situacao sit) {
        int cont = 0;
        for (Roupa r : roupas) {
            if (r.situacao == sit) {
                cont++;
            }
        }
        System.out.printf("Há %d roupas no(a) %s\n", cont, sit);
    }

    public static void main(String Arg[]) {
        help();
        while (true) {
            System.out.print("vest> ");
            String cmd = scan.nextLine().toUpperCase();
            switch (cmd) {
                case "HELP":
                    help();
                    System.out.println();
                    break;
                case "ADD":
                    adicionar_roupa();
                    scan.nextLine();  // gambiarra
                    System.out.println();
                    break;
                case "RM":
                    remover_roupa();
                    scan.nextLine();  // gambiarra
                    System.out.println();
                    break;
                case "COR":
                    cor_predominante_gr();
                    System.out.println();
                    break;
                case "LAV":
                    contar_roupas_em(Situacao.LAVANDERIA);
                    System.out.println();
                    break;
                case "GR":
                    contar_roupas_em(Situacao.GUARDA_ROUPAS);
                    System.out.println();
                    break;
                case "EXIT":
                    return;
                case "":
                    break;
                default:
                    System.out.println("Comando não reconhecido. Digite HELP para ver a lista de opções válidas.");
                    System.out.println();
                    break;
            }
        }
    }
}

enum Categoria {
    SOCIAL,
    ESPORTE,
    ESPORTE_FINO,
    TRABALHO
}
enum Cor {
    MARROM(0),
    VERMELHO(1),
    LARANJA(2),
    AMARELO(3),
    VERDE(4),
    CIANO(5),
    AZUL(6),
    ROXO(7),
    BRANCO(8),
    CINZA(9),
    PRETO(10);

    private int index;

    private Cor(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
enum Tecido {
    ALGODAO,
    POLIESTER,
    LATEX
}
enum Situacao {
    GUARDA_ROUPAS,
    EM_USO,
    LAVANDERIA
}
class Roupa {
    public Categoria categoria;
    public Cor cor;
    public int manequim;   // numero do manequim
    public Tecido tecido;
    public double preco;
    public Situacao situacao;

    public  Roupa(Categoria cat_, Cor cor_, int num_, Tecido tec_, double preco_, Situacao sit_) {
        categoria = cat_;
        cor = cor_;
        manequim = num_;
        tecido = tec_;
        preco = preco_;
        situacao = sit_;
    }
}
