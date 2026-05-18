package com.ScholarShare.util;

import jakarta.servlet.ServletContext;

import java.io.File;

/**
 * Resolves the on-disk upload directory so that uploaded files are written to
 * src/main/webapp/uploads/... and survive Tomcat redeployments.
 *
 * <p>The base path is read from the {@code uploadBasePath} context-param in
 * web.xml.  If that param is absent (e.g. a production deployment where the
 * WAR is already exploded), we fall back to {@code getRealPath("")} which
 * points to the exploded WAR root — the same behaviour as before.</p>
 */
public final class FileUploadUtil {

    private FileUploadUtil() {}

    /**
     * Returns the absolute path to the upload sub-directory, creating it if
     * it does not already exist.
     *
     * @param ctx        the servlet context
     * @param subDir     relative sub-directory, e.g. {@code "uploads/resources"}
     * @return           absolute {@link File} for the directory
     */
    public static File resolveUploadDir(ServletContext ctx, String subDir) {
        String base = ctx.getInitParameter("uploadBasePath");
        if (base == null || base.isBlank()) {
            // Fallback: use the exploded WAR root (works in production)
            base = ctx.getRealPath("");
        }

        File dir = new File(base, subDir.replace("/", File.separator));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
