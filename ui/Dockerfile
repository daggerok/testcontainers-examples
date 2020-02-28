FROM daggerok/jboss-eap-6.4:6.4.22-alpine
HEALTHCHECK --retries=33 \
            --timeout=1s \
            --interval=1s \
            --start-period=3s \
            CMD wget -q --spider http://127.0.0.1:8080/ui/ || exit 1
COPY --chown=jboss ./target/*.war ${JBOSS_HOME}/standalone/deployments/ui.war
