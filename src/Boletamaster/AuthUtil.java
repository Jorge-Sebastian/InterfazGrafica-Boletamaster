package Boletamaster;

public class AuthUtil {

    public static Administrador loginAdmin(String login, String password) {
        if (Main.admin != null
                && Main.admin.getLogin().equals(login)
                && Main.admin.getPassword().equals(password)) {
            return Main.admin;
        }
        return null;
    }

    public static Organizador loginOrganizador(String login, String password) {
        for (Usuario u : Main.usuarios) {
            if (u instanceof Organizador
                    && u.getLogin().equals(login)
                    && u.getPassword().equals(password)) {
                return (Organizador) u;
            }
        }
        return null;
    }

    public static Cliente loginCliente(String login, String password) {
        for (Usuario u : Main.usuarios) {
            if (u instanceof Cliente
                    && u.getLogin().equals(login)
                    && u.getPassword().equals(password)) {
                return (Cliente) u;
            }
        }
        return null;
    }
}
