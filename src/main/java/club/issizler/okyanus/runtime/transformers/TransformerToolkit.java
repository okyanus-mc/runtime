package club.issizler.okyanus.runtime.transformers;

import javassist.CtClass;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TransformerToolkit {

    private static void removeInterface(CtClass c, String name) throws NotFoundException {
        CtClass[] ifaces = c.getInterfaces();
        List<CtClass> newIfaces = new ArrayList<>();

        for (CtClass iface : ifaces) {
            if (!iface.getName().equals(name))
                newIfaces.add(iface);
        }

        c.setInterfaces(newIfaces.toArray(new CtClass[0]));
    }

}
