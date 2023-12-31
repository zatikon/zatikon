///////////////////////////////////////////////////////////////////////
//      Name:   CastleArchive
//      Desc:   An archived castle
//      Date:   8/6/2008 - Gabe Jones
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

public class CastleArchive {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final String name;
    private final short size;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CastleArchive(String newName, short newSize) {
        name = newName;
        size = newSize;
    }


    /////////////////////////////////////////////////////////////////
    // String override
    /////////////////////////////////////////////////////////////////
    public String toString() {
        return name;
    }


    /////////////////////////////////////////////////////////////////
    // String override
    /////////////////////////////////////////////////////////////////
    public short size() {
        return size;
    }
}
