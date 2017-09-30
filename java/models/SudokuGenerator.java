package models;

public class SudokuGenerator {
    public static State generate(int n, int holes){
        int board[][] = new int[n][n];

        int sn = (int) Math.sqrt(n);
        
        // Genera un tablero de sudoku valido
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                int delta = sn * i + (i / sn);
                board[i][j] = (delta + j) % n + 1;
            }
        }
        
        // Permuta filas o columnas de un mismo bloque
        for(int k = 0; k < 10 * n; k++){
            int p0 = (int) Math.floor(Math.random() * n) % sn;
            int p1 = (int) Math.floor(Math.random() * n) % sn;
            int p2 = (int) Math.floor(Math.random() * n) % sn;
            if(Math.random() < 0.5){
                for(int i = 0; i < n; i++){
                    int aux = board[i][p0 + p2 * sn];
                    board[i][p0 + p2 * sn] = board[i][p1 + p2 * sn];
                    board[i][p1 + p2 * sn] = aux;
                }
            }else{
                for(int j = 0; j < n; j++){
                    int aux = board[p0 + p2 * sn][j];
                    board[p0 + p2 * sn][j] = board[p1 + p2 * sn][j];
                    board[p1 + p2 * sn][j] = aux;
                }
            }
        }

        // Permuta bloques por filas o por columnas
        for(int k = 0; k < n; k++){
            int p0 = (int) Math.floor(Math.random() * n) % sn;
            int p1 = (int) Math.floor(Math.random() * n) % sn;
            if(Math.random() < 0.5){
                for(int i = 0; i < n; i++){
                    for(int j = 0; j < sn; j++){
                        int aux = board[i][p0 * sn + j];
                        board[i][p0 * sn + j] = board[i][p1 * sn + j];
                        board[i][p1 * sn + j] = aux;
                    }
                }
            }else{
                for(int j = 0; j < n; j++){
                    for(int i = 0; i < sn; i++){
                        int aux = board[p0 * sn + i][j];
                        board[p0 * sn + i][j] = board[p1 * sn + i][j];
                        board[p1 * sn + i][j] = aux;
                    }
                }
            }
        }

        // Intercambia los numeros
        int map[] = new int[n];
        for(int i = 0; i < n; i++){
            map[i] = i + 1;
        }
        for(int k = 0; k < 100; k++){
            int p0 = (int) Math.floor(Math.random() * n);
            int p1 = (int) Math.floor(Math.random() * n);
            int aux = map[p0];
            map[p0] = map[p1];
            map[p1] = aux;
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = map[board[i][j] - 1];
            }
        }
        
        // Genera los agujeros
        for(int k = 0; k < holes; k++){
            int p0 = (int) Math.floor(Math.random() * n);
            int p1 = (int) Math.floor(Math.random() * n);
            while(board[p0][p1] == State.EMPTY){
                p0 = (int) Math.floor(Math.random() * n);
                p1 = (int) Math.floor(Math.random() * n);
            }
            board[p0][p1] = State.EMPTY;
        }
        
        State state = new State(sn);
        
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                state.set(i, j, board[i][j]);
            }
        }

        return state;
    }
}
