package com.step.strategy;
        import com.step.entity.primary.Element;
        import com.unboundid.ldap.sdk.LDAPException;
        import java.util.List;
public class StrategyContext {
    /**
     * 策略上下文
     */
   private IStrategy iStrategy;

       public StrategyContext(IStrategy  iStrategy){
            this.iStrategy = iStrategy;
       }
       public void  invoke(Element element, String baseDn, List<Element> persons) throws LDAPException {
           iStrategy.algorithmMethod(element,baseDn,persons);
}

}
