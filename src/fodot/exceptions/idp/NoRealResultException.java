package fodot.exceptions.idp;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class NoRealResultException extends IdpException {

    private static final long serialVersionUID = 1L;

    public NoRealResultException() {
        super("No sensical output was produced.");
    }
}
