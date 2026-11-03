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

import cn.hutool.core.util.URLUtil;
import cn.sycoder.sypay.tools.SypayKit;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/*
* 系统应用配置项定义Bean
*/
@Data
public class DBApplicationConfig implements Serializable {

    /** 运营系统地址 **/
    private String mgrSiteUrl;

    /** 商户系统地址 **/
    private String mchSiteUrl;

    /** 支付网关地址 **/
    private String paySiteUrl;

    /** oss公共读文件地址 **/
    private String ossPublicSiteUrl;

    /** 生成  【jsapi统一收银台跳转地址】 **/
    public String genUniJsapiPayUrl(String payOrderId){
        return getPaySiteUrl() + "/cashier/index.html#/hub/" + SypayKit.aesEncode(payOrderId);
    }

    /** 生成  【jsapi统一收银台】oauth2获取用户ID回调地址 **/
    public String genOauth2RedirectUrlEncode(String payOrderId){
        return URLUtil.encodeAll(getPaySiteUrl() + "/cashier/index.html#/oauth2Callback/" + SypayKit.aesEncode(payOrderId));
    }

    /** 生成  【商户获取渠道用户ID接口】oauth2获取用户ID回调地址 **/
    public String genMchChannelUserIdApiOauth2RedirectUrlEncode(JSONObject param){
        return URLUtil.encodeAll(getPaySiteUrl() + "/api/channelUserId/oauth2Callback/" + SypayKit.aesEncode(param.toJSONString()));
    }

    /** 生成  【jsapi统一收银台二维码图片地址】 **/
    public String genScanImgUrl(String url){
        return getPaySiteUrl() + "/api/scan/imgs/" + SypayKit.aesEncode(url) + ".png";
//        return getPaySiteUrl() + "/api/scan/imgs/" + url + ".png";
    }

    /** 生成  【支付宝 isv子商户的授权链接地址】 **/
    public String genAlipayIsvsubMchAuthUrl(String isvNo, String mchAppId){
        return getPaySiteUrl() + "/api/channelbiz/alipay/redirectAppToAppAuth/" + isvNo + "_" + mchAppId;
    }

}
