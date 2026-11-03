 /*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sycoder.sypay.model;

 import cn.sycoder.contants.CS;
 import cn.sycoder.sypay.model.config.ChannelCertConfigKitBean;
 import cn.sycoder.sypay.model.config.SpringBeansUtil;
 import cn.sycoder.sypay.model.params.alipay.AlipayIsvParams;
 import cn.sycoder.sypay.model.params.alipay.AlipayNormalMchParams;
 import com.alipay.api.*;
 import cn.sycoder.sypay.model.params.alipay.AlipayConfig;
 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.extern.slf4j.Slf4j;

 /*
 * 支付宝Client 包装类
 */
 @Slf4j
 @Data
 @AllArgsConstructor
 public class AlipayClientWrapper {
	 public static final Byte NO = 0;
	 public static final Byte YES = 1;

     //默认为 不使用证书方式
     private Byte useCert = NO;

     /** 缓存支付宝client 对象 **/
     private AlipayClient alipayClient;

     /** 封装支付宝接口调用函数 **/
     public <T extends AlipayResponse> T execute(AlipayRequest<T> request){

         try {

             T alipayResp = null;

             if(useCert != null && useCert == YES){ //证书加密方式
                 alipayResp = alipayClient.certificateExecute(request);

             }else{ //key 或者 空都为默认普通加密方式
                 alipayResp = alipayClient.execute(request);
             }

             return alipayResp;

         } catch (AlipayApiException e) { // 调起接口前出现异常，如私钥问题。  调起后出现验签异常等。

             log.error("调起支付宝execute[AlipayApiException]异常！", e);
             //如果数据返回出现验签异常，则需要抛出： UNKNOWN 异常。
             throw new RuntimeException(e.getMessage());

         } catch (Exception e) {
             log.error("调起支付宝execute[Exception]异常！", e);
             throw new RuntimeException("调用支付宝client服务异常");
         }
     }



     /*
      * 构建支付宝client 包装类
      */
     public static AlipayClientWrapper buildAlipayClientWrapper(Byte useCert, Byte sandbox, String appId, String privateKey, String alipayPublicKey, String signType, String appCert,
                                                                                         String alipayPublicCert, String alipayRootCert){

         //避免空值
         sandbox = sandbox == null ? NO : sandbox;

         AlipayClient alipayClient = null;
         if(useCert != null && useCert == YES){ //证书的方式

             ChannelCertConfigKitBean channelCertConfigKitBean = SpringBeansUtil.getBean(ChannelCertConfigKitBean.class);

             CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
             certAlipayRequest.setServerUrl(sandbox == YES ? AlipayConfig.SANDBOX_SERVER_URL : AlipayConfig.PROD_SERVER_URL);
             certAlipayRequest.setAppId(appId);
             certAlipayRequest.setPrivateKey(privateKey);
             certAlipayRequest.setFormat(AlipayConfig.FORMAT);
             certAlipayRequest.setCharset(AlipayConfig.CHARSET);
             certAlipayRequest.setSignType(signType);

             certAlipayRequest.setCertPath(channelCertConfigKitBean.getCertFilePath(appCert));
             certAlipayRequest.setAlipayPublicCertPath(channelCertConfigKitBean.getCertFilePath(alipayPublicCert));
             certAlipayRequest.setRootCertPath(channelCertConfigKitBean.getCertFilePath(alipayRootCert));
             try {
                 alipayClient = new DefaultAlipayClient(certAlipayRequest);
             } catch (AlipayApiException e) {
                 log.error("error" ,e);
                 alipayClient = null;
             }
         }else{
             alipayClient = new DefaultAlipayClient(sandbox == YES ? AlipayConfig.SANDBOX_SERVER_URL : AlipayConfig.PROD_SERVER_URL
                     , appId, privateKey, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                     alipayPublicKey, signType);
         }

         return new AlipayClientWrapper(useCert, alipayClient);
     }




     public static AlipayClientWrapper buildAlipayClientWrapper(AlipayIsvParams alipayParams){

         return buildAlipayClientWrapper(
                 alipayParams.getUseCert(), alipayParams.getSandbox(), alipayParams.getAppId(), alipayParams.getPrivateKey(),
                 alipayParams.getAlipayPublicKey(), alipayParams.getSignType(), alipayParams.getAppPublicCert(),
                 alipayParams.getAlipayPublicCert(), alipayParams.getAlipayRootCert()
         );
     }

     public static AlipayClientWrapper buildAlipayClientWrapper(AlipayNormalMchParams alipayParams){

         return buildAlipayClientWrapper(
                 alipayParams.getUseCert(), alipayParams.getSandbox(), alipayParams.getAppId(), alipayParams.getPrivateKey(),
                 alipayParams.getAlipayPublicKey(), alipayParams.getSignType(), alipayParams.getAppPublicCert(),
                 alipayParams.getAlipayPublicCert(), alipayParams.getAlipayRootCert()
         );
     }

































 }
