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
package cn.sycoder.sypay.payorder.channel.alipay;

import cn.sycoder.contants.CS;
import cn.sycoder.sypay.model.MchAppConfigContext;
import cn.sycoder.sypay.model.params.alipay.AlipayConfig;
import cn.sycoder.sypay.model.params.alipay.AlipayIsvParams;
import cn.sycoder.sypay.model.params.alipay.AlipayNormalMchParams;
import cn.sycoder.sypay.payorder.channel.IChannelUserService;
import cn.sycoder.sypay.payorder.service.impl.ConfigContextQueryService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 支付宝： 获取用户ID实现类
*/
@Service
@Slf4j
public class AlipayChannelUserService implements IChannelUserService {

    @Autowired
    private ConfigContextQueryService configContextQueryService;

    @Override
    public String getIfCode() {
        return CS.IF_CODE.ALIPAY;
    }

    @Override
    public String buildUserRedirectUrl(String callbackUrlEncode, MchAppConfigContext mchAppConfigContext) {

        String oauthUrl = AlipayConfig.PROD_OAUTH_URL;
        String appId = null;

        if(mchAppConfigContext.isIsvsubMch()){
            AlipayIsvParams isvParams = (AlipayIsvParams) configContextQueryService.queryIsvParams(mchAppConfigContext.getMchInfo().getServerNo(), getIfCode());
            if(isvParams == null) {
                 throw new ServiceException("服务商支付宝接口没有配置！");
            }
            appId = isvParams.getAppId();
            if(isvParams.getSandbox() != null && isvParams.getSandbox() == 1){
                oauthUrl = AlipayConfig.SANDBOX_OAUTH_URL;
            }
        }else{
            //获取商户配置信息
            AlipayNormalMchParams normalMchParams = (AlipayNormalMchParams) configContextQueryService.queryNormalMchParams(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), getIfCode());
            if(normalMchParams == null) {
                 throw new ServiceException("商户支付宝接口没有配置！");
            }
            appId = normalMchParams.getAppId();
            if(normalMchParams.getSandbox() != null && normalMchParams.getSandbox() == 1){
                oauthUrl = AlipayConfig.SANDBOX_OAUTH_URL;
            }
        }
        String alipayUserRedirectUrl = String.format(oauthUrl, appId, callbackUrlEncode);
        log.info("alipayUserRedirectUrl={}", alipayUserRedirectUrl);
        return alipayUserRedirectUrl;
    }

    @Override
    public String getChannelUserId(JSONObject reqParams, MchAppConfigContext mchAppConfigContext) {

        String authCode = reqParams.getString("auth_code");

        //通过code 换取openId
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode); request.setGrantType("authorization_code");
        try {
            return configContextQueryService.getAlipayClientWrapper(mchAppConfigContext).execute(request).getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
