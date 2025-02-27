1. 通过注解 EnableDynamicTp import 得一个 DtpConfigurationSelector 这个selector 会导入 下面三个类
   DtpBaseBeanDefinitionRegistrar.class.getName(),
   DtpBeanDefinitionRegistrar.class.getName(),
   DtpBaseBeanConfiguration.class.getName()
其中的 DtpBeanDefinitionRegistrar 会将配置文件中配置的线程池，注册为beanDefinition，如果BeanDefinition中，已经有存在的相同的BeanDefinition的信息了
会将其移除用 dytp 的。其使用的是Spring 比较推荐的 GenericBeanDefinition ，其兼具RootBeanDefinition和childDefinition的能力。



背景：
spring 扫描 BeanDefinition 回从启动类开始，开始执行 @ComponentScan ，然后扫描等到得类看是否被 直接或者间接得被@Component修饰，并且进行 @Conditional 得判断，如果是满足上面得两个个条件得
那么则这个扫描所得先加入BeanDefinition，随后再判断其是否是被@Configuration修饰，如果呗configuration修饰，则递归作为初始类（相当于之前得启动类）再走一遍上面得流程
完了之后，则处理 selectImport 这里面有分三种，一种导入是一个selectImport得类，那么直接继续递归走上面得 处理selectImport得接口，如果是是 DeferredImportSelector 则再所有得处理完之后再性处理
如果是普通类，则直接形成 ConfigurationClass 然后加入当前 parser 的 ConfigurationClass，随后在 this.reader.loadBeanDefinitions(configClasses);处理为BeanDefinition
如果是 BeanDefinitionRegistrar 那么则会加入，引入的他注解所标识的类的ConfigurationClass中
处理完 SelectImport 之后。再处理@Bean的解析，@Bean解析出来的信息也是放在他锁在的Config类的ConfigurationClass的对象中。最终解析了一堆 ConfigurationClass 之后，执行 DeferredImportSelector 将其中的注入的类再调用 前面处理selectImport 的方法。


启动类的 ConfigurationClass 会在最后加入 parser的ConfigurationClass对象的的集合，这保证了启动类中表明的import会在相对我们定义的类后面处理。
其次 dytp 注册 bean 用得 ImportSelector 是 一个 DeferredImportSelector，这也会保证这个 import 所归属的config类所关联的所有的类， 都处理完之后再加入这个config类的 BeanDefinitionRegistrar 的集合，保证注册器靠后
在最后  this.reader.loadBeanDefinitions(configClasses) 处理的时候，靠后一点产生 BeanDefinition ， 因为这个 DtpBeanDefinitionRegistrar 在产生 BeanDefinition 的时候，会先检查BeanDefinition的容器，是否有如果有则移除加入，最大程度的避免 用户定义的配置的 Bean 和 dytp根据配置产生的bean得冲突 的冲突。

因为 DtpConfigurationSelector 是一个 DeferredImportSelector ，DeferredImportSelector 会在归属得 @Configuration 或者 @Component 得类得所有得@Bean @Component @Import @PropertySource等都处理完之后再执行这个selectImport
然后这个import 导入 又是一个 DtpBeanDefinitionRegistrar 这个是，官方也建议在启动类上加 @EnableDynamicTp 这个注解，而启动类又是作为所有扫描得起点，也就是等所有得

2. 