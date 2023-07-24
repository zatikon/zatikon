///////////////////////////////////////////////////////////////////////
// Name: TargetType
// Desc: Targetting types
// Date: 5/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports


public class TargetType {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final short ANY_LINE = 0;
    public static final short ANY_LINE_JUMP = 1;
    public static final short ANY_AREA = 2;
    public static final short LOCATION_LINE = 3;
    public static final short LOCATION_LINE_JUMP = 4;
    public static final short LOCATION_AREA = 5;
    public static final short UNIT_LINE = 6;
    public static final short UNIT_LINE_JUMP = 7;
    public static final short UNIT_AREA = 8;
    public static final short NONE = 9;
    public static final short CASTLE = 10;


    /////////////////////////////////////////////////////////////////
    // Action type constants
    /////////////////////////////////////////////////////////////////
    public static final short FRIENDLY = 0;
    public static final short ENEMY = 1;
    public static final short BOTH = 2;
    public static final short MOVE = 3;

}
