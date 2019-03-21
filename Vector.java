
/**
 * Write a description of class Vector here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Vector
{
    // instance variables - replace the example below with your own
    private String token;
    private int termNo;
    private double[] vectorModel;
    private double score;

    public Vector()
    {}

    public Vector(double[] vectorModel) {
        this.vectorModel = vectorModel;
    }

    public int getTermNo()
    {
        return termNo;
    }
    
    public double getScore()
    {
        return score;
    }

    public void setTermNo(int newNo)
    {
        termNo = newNo;
    }

    public void setScore(double newScore)
    {
        score = newScore;
    }

    public double[] getModel()
    {
        return vectorModel;
    }

    public void setModel(int index, double element)
    {
        vectorModel[index] = vectorModel[index] + element;
    }
    
    public double getElement(int index)
    {
        return vectorModel[index];
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double[] getVectorModel() {
        return vectorModel;
    }

    public void setVectorModel(double[] vectorModel) {
        this.vectorModel = vectorModel;
    }
}
