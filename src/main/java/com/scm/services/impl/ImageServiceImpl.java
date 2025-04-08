package com.scm.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	
	private Cloudinary cloudinary;
	
	// ye kam autowire ki help se bhi kr skte the (constructor)
	public ImageServiceImpl(Cloudinary cloudinary) {
		this.cloudinary= cloudinary;
	}
	
	@Override
	public String uploadImage(MultipartFile contactImage,String filename) {
		
		// code likhna hai jo image ko upload kr rha ho server pe (like:-AWS,CLOUDINARY )
		
		
	
		
		try {
			
			byte[] data =new byte[contactImage.getInputStream().available()];
			
			contactImage.getInputStream().read(data);
			cloudinary.uploader().upload(data, ObjectUtils.asMap(
					"public_id",filename
					));
			
			return this.getUrlFromPublicId(filename);
			
	 	   }
		catch (IOException e) {
			
			e.printStackTrace();
			
			return null;
		}
		
		// and return kr rha hoga : url
		
		
	}

	@Override
	public String getUrlFromPublicId(String publicId) {
		
		
		return cloudinary.url()
				.transformation(
						new Transformation<>()
						  .width(AppConstants.CONTACT_IMAGE_WIDTH)
						  .height(AppConstants.CONTACT_IMAGE_HEIGHT)
						  .crop(AppConstants.CONTACT_IMAGE_CROP))
				.generate(publicId);
		 
	}

}
