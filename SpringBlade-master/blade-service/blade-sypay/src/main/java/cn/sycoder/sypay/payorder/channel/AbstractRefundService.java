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
package cn.sycoder.sypay.payorder.channel;


import cn.sycoder.sypay.model.config.ChannelCertConfigKitBean;
import cn.sycoder.sypay.payorder.service.impl.ConfigContextQueryService;
import cn.sycoder.sypay.payorder.service.impl.SysConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/*
* 退款接口抽象类
*/
public abstract class AbstractRefundService implements IRefundService{

    @Autowired
    protected SysConfigServiceImpl sysConfigService;
    @Autowired(required = false)
    protected ChannelCertConfigKitBean channelCertConfigKitBean;
    @Autowired
    protected ConfigContextQueryService configContextQueryService;

    protected String getNotifyUrl(){
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl() + "/api/refund/notify/" + getIfCode();
    }

    protected String getNotifyUrl(String refundOrderId){
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl() + "/api/refund/notify/" + getIfCode() + "/" + refundOrderId;
    }

}
